
var APP = {
};

$(document).ready(function() {
    APP.init();
});

APP.log = function (msg) {
    if ((typeof console !== 'undefined') && console.log) {
        console.log(msg);
    }
};

APP.getQueryParams = function () {
    var qvars = [],
        searchPartIdx,
        searchPart,
        hash,
        hashes,
        idx = 0,
        thisHref = (window.location.href);
    APP.log('[INFO] href = ' + thisHref);
    searchPartIdx = thisHref.indexOf('?');
    searchPart = null;
    if (searchPartIdx > -1) {
        searchPart = thisHref.slice(searchPartIdx + 1);
    }
    APP.log('[INFO] searchPart = ' + searchPart);
    hashes = null;
    if (searchPart !== null) {
        hashes = searchPart.split('&');
    }
    for (idx = 0; hashes !== null && idx < hashes.length; idx += 1) {
        hash = hashes[idx].split('=');
        qvars.push(hash[0]);
        qvars[hash[0]] = hash[1];
        APP.log('[INFO] query (key, val) = (' + hash[0] + ', ' + hash[1] + ')');
    }
    return qvars;
};

APP.getSizeExtent = function (size) {
    return (2 * Math.sqrt(size.width * size.height / Math.PI));
};

APP.getEtcPathWith = function (path) {
	var href = (window.location.href),
		pathWithEtc;
	if (href.indexOf('file:') !== 0) {
		pathWithEtc = '../etc/' + path;
	} else {
		pathWithEtc = 'etc/' + path;
	}
	return pathWithEtc;
};

APP.cfg = {
    queryParams: [],
    switchesPath: APP.getEtcPathWith('ryu/topology/switches'),
    linksPath:    APP.getEtcPathWith('ryu/topology/links'),
    topoConfPath: APP.getEtcPathWith('ld/topo'),
    svgSize: {width: 708, height: 416},
    portRectSize: {width: 8, height: 8},
    nodeRectSize: {width: 36, height: 36},
    linkLabelRectSize: {width: 32, height: 16},
    nodeCircleSize: {r: 20}
};


APP.model = {
    topoSwitches: [],
    topoLinks: [],
    ldTopoConf: {},
    nodes: [],
    links: [],
    ports: []
};

APP.model.update = function (switches, links, topoConf) {
    var searchSwitchIndexByDpid = function (dpid, model) {
        var idxSwitch = 0,
            idxPort = 0,
            idx,
            oSwitch,
            oPort;
        for (idxSwitch = 0; idxSwitch < model.topoSwitches.length; idxSwitch += 1) {
            oSwitch = model.topoSwitches[idxSwitch];
            for (idxPort = 0; idxPort < oSwitch.ports.length; idxPort += 1) {
                oPort = oSwitch.ports[idxPort];
                if (oPort.dpid === dpid) {
                    idx = idxSwitch;
                    break;
                }
            }
            if (idx) {
                break;
            }
        }
        return idx;
    };
    var normalizeTopoConfDpid = function (dpid) {
        var splited = dpid.split('.', 2),
            prefix = ('0000' + splited[0]).slice(-4),
            seq = splited[1].split(':'),
            idx = 0,
            suffix = '';
        for (idx = 0; idx < seq.length; idx += 1) {
            suffix += ('00' + seq[idx]).slice(-2);
        }
        return (prefix + suffix);
    };
    var searchNodeFromTopoConf = function (dpid, topoConf) {
        var node = null,
            idx = 0,
            nodes,
            len;

        nodes = topoConf.switches;
        len = nodes.length;
        for (idx = 0; idx < len && !node; idx += 1) {
            if (dpid === normalizeTopoConfDpid(nodes[idx].dpid)) {
                node = nodes[idx];
            }
        }

        nodes = topoConf.hosts;
        len = nodes.length;
        for (idx = 0; idx < len && !node; idx += 1) {
            if (dpid === normalizeTopoConfDpid(nodes[idx].dpid)) {
                node = nodes[idx];
            }
        }
        return node;
    };

    var searchLdBridgeByName = function (brName, topoConf) {
        var ldBridges = topoConf.bridges,
            ldBridge,
            idx = 0;
        for (idx = 0; idx < ldBridges.length; idx += 1) {
            if (brName && ldBridges[idx].name === brName) {
                ldBridge = ldBridges[idx];
                break;
            }
        }
        return ldBridge;
    };

    this.topoSwitches = switches;
    this.topoLinks = links;
    this.ldTopoConf = topoConf;

    this.nodes = (function (model) {
        var objs = [],
            sw,
            topoConfNode,
            idx = 0;
        for (idx = 0; idx < model.topoSwitches.length; idx += 1) {
            sw = model.topoSwitches[idx];
            topoConfNode = searchNodeFromTopoConf(sw.dpid, model.ldTopoConf);
            if (!topoConfNode) {
                topoConfNode = {};
            }
            topoConfNode.topoSwitch = sw;
            objs.push(topoConfNode);
        }
        return objs;
    }(this));

    this.links = (function (model) {
        var objs = [],
            idxLink = 0,
            link,
            srcRySwIdx = 0, dstRySwIdx = 0, srcPortIdx, dstPortIdx,
            srcRySw, dstRySw, srcLdNode, dstLdNode,
            ldBridge, 
            nodeToNodeKey, nodeToNodeCounts = {}, 
            linkKey, linkKeys = [],
            topoLink;
        for (idxLink = 0; idxLink < model.topoLinks.length; idxLink += 1) {
            topoLink = model.topoLinks[idxLink];
            srcRySwIdx = searchSwitchIndexByDpid(topoLink.src.dpid, model);
            dstRySwIdx = searchSwitchIndexByDpid(topoLink.dst.dpid, model);
            srcPortIdx = parseInt(topoLink.src.port_no) - 2;
            dstPortIdx = parseInt(topoLink.dst.port_no) - 2;
            srcRySw = model.topoSwitches[srcRySwIdx];
            dstRySw = model.topoSwitches[dstRySwIdx];
            srcLdNode = searchNodeFromTopoConf(srcRySw.dpid, model.ldTopoConf);
            dstLdNode = searchNodeFromTopoConf(dstRySw.dpid, model.ldTopoConf);
            ldBridge = searchLdBridgeByName(srcLdNode.brNames[srcPortIdx], model.ldTopoConf);

            //APP.log('src: ' + srcLdNode.name + ', ' + srcLdNode.brNames[srcPortIdx]);
            //APP.log('dst: ' + dstLdNode.name + ', ' + dstLdNode.brNames[dstPortIdx]);
            if (srcLdNode.brNames[srcPortIdx] !== dstLdNode.brNames[dstPortIdx]) {
                APP.log('[ERROR] Unexpected situation. Bridge names should be same, but not.');
            }

            if (srcRySwIdx < dstRySwIdx) {
                //nodeToNodeKey = '' + srcRySwIdx + '-' + dstRySwIdx;
                nodeToNodeKey = '' + srcLdNode.name + '-' + dstLdNode.name;
            } else {
                //nodeToNodeKey = '' + dstRySwIdx + '-' + srcRySwIdx;
                nodeToNodeKey = '' + dstLdNode.name + '-' + srcLdNode.name;
            }
            linkKey = ldBridge.name + '-' + nodeToNodeKey;

            if (linkKeys.indexOf(linkKey) > -1) {
                //APP.log('linkKey already contains: ' + linkKey);
                continue;
            } else {
                linkKeys.push(linkKey);
            }

            //APP.log('nodeToNodeKey: ' + nodeToNodeKey);
            if (nodeToNodeCounts[nodeToNodeKey] !== undefined) {
                nodeToNodeCounts[nodeToNodeKey] += 1;
                APP.log('Multiple nodeToNode: (nodeToNodeKey, count) = (' + nodeToNodeKey + ', ' + nodeToNodeCounts[nodeToNodeKey] + ')');
            } else {
                nodeToNodeCounts[nodeToNodeKey] = 0;
            }

            link = {
                source: srcRySwIdx,
                target: dstRySwIdx,
                topoLink: topoLink,
                ldBridge: ldBridge,
                nodeToNodeIndex: nodeToNodeCounts[nodeToNodeKey],
                dVec: function () {
                    var r,
                        enorm, factor,
                        dfactor = 6,
                        dvecx = 0.0, dvecy = 0.0,
                        idx = this.nodeToNodeIndex;
                    if (idx > 0) {
                        r = -1.0 * (this.target.x - this.source.x) / (this.target.y - this.source.y);
                        factor = -1.0 * ((idx + 1) * 0.5) * (idx * 0.5 + 1.0 * (idx % 2)) * dfactor;
                        enorm = Math.sqrt(1.0 + r * r);
                        dvecx = factor * 1.0 / enorm;
                        dvecy = factor * r / enorm;
                    }
                    return {dx: dvecx, dy: dvecy};
                }
            };
            objs.push(link);
        }
        return objs;
    }(this));

    this.ports = (function (model) {
        var objs = [],
            idxLink = 0,
            link,
            nodeExtent = APP.getSizeExtent(APP.cfg.nodeRectSize),
            portExtent = APP.getSizeExtent(APP.cfg.portRectSize);
        var createPort = function (base, link, main, sub) {
            var obj = base;
            obj.link = link;
            obj.getPos = function () {
                var lenDx = (link[main].x - link[sub].x),
                    lenDy = (link[main].y - link[sub].y),
                    linkLen = Math.sqrt(lenDx * lenDx + lenDy * lenDy),
                    weight = (1.0 - 0.5 * (nodeExtent + portExtent * 0.75) / linkLen),
                    x0 = link[main].x * weight,
                    y0 = link[main].y * weight,
                    dx = link[sub].x * (1.0 - weight) + link.dVec().dx,
                    dy = link[sub].y * (1.0 - weight) + link.dVec().dy;
                return {x: (x0 + dx), y: (y0 + dy)};
            };
            return obj;
        };
        for (idxLink = 0; idxLink < model.links.length; idxLink += 1) {
            link = model.links[idxLink];
            objs.push(createPort(link.topoLink.src, link, 'source', 'target'));
            objs.push(createPort(link.topoLink.dst, link, 'target', 'source'));
        }
        return objs;
    }(this));

    APP.view.update(this);
};

APP.model.setSelectedFlowTypeName = function (flowTypeName) {
    var idx = 0, 
        isFlowTypeNameFound = false, 
        flows;
   
    APP.log('BEGIN setSelectedFlowTypeName');
    flows = this.ldTopoConf.flows;
    for (idx = 0; idx < flows.length; idx += 1) {
        if (flows[idx].name === flowTypeName) {
            APP.view.onLinkSelectedWithBrNames(flows[idx].brNames);
            isFlowTypeNameFound = true;
        }
    }
    if (!isFlowTypeNameFound) {
        APP.view.onLinkSelectedWithBrNames([]);
    }
    APP.log('END setSelectedFlowTypeName');
};

APP.view = {
    viewNodes: {},
    viewLinks: {},
    viewPorts: {},

    canvas: {},
    svg: {},
    force: {},
    tooltip: {},

    $flowListPanel: {},
    $flowListTemplate: {}
};

APP.view.init = function () {
    var svgSize = APP.cfg.svgSize;

    this.$flowListPanel = $('.flow-list-panel');
    this.$flowListTemplate = $('.flow-list-template');

    this.canvas = d3.select('#topo-canvas');
    this.svg = this.canvas.append('svg');
    this.force = d3.layout.force();

    this.viewNodes = this.svg.selectAll('.view-node');
    this.viewLinks = this.svg.selectAll('.view-link');
    this.viewPorts = this.svg.selectAll('.view-port');

    this.svg
        .attr('width', svgSize.width)
        .attr('height', svgSize.height);

    this.force
        .size([svgSize.width, svgSize.height])
        .linkDistance(APP.getSizeExtent(svgSize) / 8.5)
        .charge(function (d) {
            var chg;
            if ('host' === d.type) {
                chg = -200;
            } else {
                chg = -500;
            }
            return chg;
        })
        .friction(0.75);

    this.tooltip = this.canvas.append('div').attr('class', 'tooltip').text('');

    (function (view) {
        view.$flowListPanel.accordion({
            collapsible: true,
            active: false,
            hightstyle: 'fill'
        });
        view.$flowListPanel.position({
            of: $('#topo-canvas'),
            my: 'left top',
            at: 'left top',
            collision: 'none none'
        });
        view.setUpFlowListAsSelectable(view.$flowListPanel.find('ul'));

    }(this));

    //this.setSampleFlowListItems();
    //window.APP.view.setFlowListItems('slice-1', [{flowName: 'flow-1', flowTypeName: 'osaka11slow'},{flowName: 'flow-2', flowTypeName: 'akashi23cutthrough'}]);
    window.APP.view.setFlowListItems(null, null);
};

APP.view.setSampleFlowListItems = function () {
    this.setFlowListItems('slice-1', [
        {flowName: 'flow-1-1', flowTypeName: 'of1'},
        {flowName: 'flow-1-2', flowTypeName: 'of2'},
        {flowName: 'flow-1-3', flowTypeName: 'of3'},
        {flowName: 'flow-1-4', flowTypeName: 'of4'}
    ]);
};

APP.view.update = function (model) {
    var that = this;
    var nodes = model.nodes,
        links = model.links,
        ports = model.ports;

    var tick = function () {
        var nodeRectSize = APP.cfg.nodeRectSize;
        that.viewLinks
            .attr('x1', function (d) { return (d.source.x + d.dVec().dx); })
            .attr('y1', function (d) { return (d.source.y + d.dVec().dy); })
            .attr('x2', function (d) { return (d.target.x + d.dVec().dx); })
            .attr('y2', function (d) { return (d.target.y + d.dVec().dy); });

        that.viewNodes
            .attr('transform', function (d) {
                return ('translate('
                    + (d.x - nodeRectSize.width / 2) + ','
                    + (d.y - nodeRectSize.height / 2) + ')');
            });

        that.viewPorts
            .attr('transform', function (d) {
                var pos = d.getPos(),
                    size = APP.cfg.portRectSize;
                return 'translate('
                    + (pos.x - size.width / 2) + ','
                    + (pos.y - size.height / 2) + ')';
            });
    };

    var updateViewLinks = function () {
        var lines, g;
        that.viewLinks = that.viewLinks.data(links);
        that.viewLinks.exit().remove();
        lines = that.viewLinks.enter().append('line')
            .attr('class', function () { return 'view-link'; });

        g = lines;
        g.on('mouseover', function () {
            return that.tooltip.style('visibility', 'visible');
        });
        g.on('mousemove', function (d) {
            return that.tooltip
                .style('top', (d3.event.pageY - 10) + 'px')
                .style('left', (d3.event.pageX + 10) + 'px')
                .html('<h1>Channel</h1>'
                    + '<dl>'
                    + '<dt>Name:</dt><dd>' + d.ldBridge.name + '</dd>'
                    + '</dl>');
        });
        g.on('mouseout', function () {
            return that.tooltip.style('visibility', 'hidden');
        });
    };

    var updateViewNodes = function () {
        var g,
            //r = APP.cfg.nodeCircleSize.r,
            size = APP.cfg.nodeRectSize;

        that.viewNodes = that.viewNodes.data(nodes);
        that.viewNodes.exit().remove();
        g = that.viewNodes.enter().append('g');
        g.attr('class', function (d) { return 'view-node-' + d.type; });

        g.call(that.force.drag().on('dragstart', function (d) {
            d.fixed = true;
            d3.select(this).classed('fixed', d.fixed);
        }));
        g.on('dblclick', function (d) {
            d.fixed = false;
            d3.select(this).classed('fixed', d.fixed);
        });
        g.on('mouseover', function () {
            return that.tooltip.style('visibility', 'visible');
        });
        g.on('mousemove', function (d) {
            var ipdisp = '', macdisp = '';
            if (d.ip !== null) {
                ipdisp = '<dt>IP:</dt><dd>' + d.ip + '</dd>';
            }
            if (d.mac !== null) {
                macdisp = '<dt>MAC:</dt><dd>' + d.mac + '</dd>';
            }
            return that.tooltip
                .style('top', (d3.event.pageY - 10) + 'px')
                .style('left', (d3.event.pageX + 10) + 'px')
                .html('<h1>Node</h1>'
                    + '<dl>'
                    + '<dt>Name:</dt><dd>' + d.name + '</dd>'
                    + '<dt>Type:</dt><dd>' + d.type + '</dd>'
                    + '<dt>DP ID:</dt><dd>' + d.dpid + '</dd>'
                    + ipdisp
                    + macdisp
                    + '</dl>');
        });
        g.on('mouseout', function () {
            return that.tooltip.style('visibility', 'hidden');
        });

//        g.append('circle')
//        .attr('r', function (d) {return r;});

        g.append('rect')
            .attr('rx', size.width * 0.2)
            .attr('ry', size.height * 0.2)
            .attr('width', size.width)
            .attr('height', size.height);

        g.append('text')
            .attr('dx', size.width / 2)
            .attr('dy', size.height / 2)
            .text(function (d) { return d.name; });
    };

    var updateViewPorts = function () {
        var g,
            size = APP.cfg.portRectSize;
        that.viewPorts = that.viewPorts.data(ports);
        that.viewPorts.exit().remove();
        g = that.viewPorts.enter().append('g');
        g.attr('class', function () { return 'view-port'; });
        g.on('mouseover', function () {
            return that.tooltip.style('visibility', 'visible');
        });
        g.on('mousemove', function (d) {
            return that.tooltip
                .style('top', (d3.event.pageY - 10) + 'px')
                .style('left', (d3.event.pageX + 10) + 'px')
                .html('<h1>Port</h1>'
                    + '<dl>'
                    + '<dt>Name:</dt><dd>' + d.name + '</dd>'
                    + '</dl>');
        });
        g.on('mouseout', function () {
            return that.tooltip.style('visibility', 'hidden');
        });

        g.append('rect')
            .attr('width', size.width).attr('height', size.height);

        //g.append('text')
        //.text(function (d) {return d.name;})
        //.attr('dx', 2).attr('dy', size.height  - 6);
    };

    this.force
        .on('tick', tick)
        .nodes(nodes).links(links);
    this.force.start();

    updateViewLinks();
    updateViewNodes();
    updateViewPorts();
};

APP.view.onLinkSelectedWithBrNames = function (brNames) {
    var idx = 0, brName;
    APP.log('onLinkSelectedWithBrNames (' + brNames + ')');
    this.viewLinks.classed('view-link-selected', function (d) {
        return (brNames.indexOf(d.ldBridge.name) > -1);
    });
};

APP.view.setAllLinksToUnselected = function () {
    this.onLinkSelectedWithBrNames([]);
};

APP.view.setUpFlowListAsSelectable = function ($flowList) {
    $flowList.selectable({
        stop: function () {
            $('.ui-selected .flow-type-name', this).each(function () {
                var flowTypeName = $(this).text();
                APP.log('Flow type name selected : ' + $(this).text());
                APP.model.setSelectedFlowTypeName(flowTypeName);
            });
        }
    });
};

APP.view.setFlowListItems = function (sliceName, flowListItems) {
    var $titleHeadline = this.$flowListTemplate.find('h3:first').clone(),
        $flowList = this.$flowListTemplate.find('ul:first').clone(),
        $flowItemTpl = this.$flowListTemplate.find('ul li:first'),
        $flowItem,
        idx = 0;

    this.$flowListPanel.find('> *').remove();

    $flowList.find('li').remove();

    if (sliceName && sliceName.length > 0) {
        $titleHeadline.text(sliceName);
        $titleHeadline.appendTo(this.$flowListPanel);
    }

    for (idx = 0; flowListItems && idx < flowListItems.length; idx += 1) {
       $flowItem = $flowItemTpl.clone();
       $flowItem.find('.flow-name').text(flowListItems[idx].flowName);
       $flowItem.find('.flow-type-name').text(flowListItems[idx].flowTypeName);
       $flowItem.appendTo($flowList);
    }
    if (flowListItems) {
        $flowList.appendTo(this.$flowListPanel);
    }

    this.setUpFlowListAsSelectable($flowList);

    this.$flowListPanel.accordion('refresh');
};

APP.api = {};

APP.api.setFlowListItems = function (sliceName, flowListItems) {
    APP.view.setAllLinksToUnselected();
    APP.view.setFlowListItems(sliceName, flowListItems);
};

APP.load = function () {
    var obj = {};
    var topoConfLoaded = function (err, data) {
        if (!err) {
            obj.topoConf = data;
            APP.model.update(obj.switches, obj.links, obj.topoConf);
        } else {
            APP.log('[ERROR] Failed to load topoConf.');
        }
    };
    var linksLoaded = function (err, data) {
        var nextUrl = APP.cfg.topoConfPath;
        if (!err) {
            obj.links = data;

            APP.log('[INFO] Loading ... ' + nextUrl);
            d3.json(nextUrl, topoConfLoaded);
        } else {
            APP.log('[ERROR] Failed to load links.');
        }
    };
    var switchesLoaded = function (err, data) {
        var nextUrl = APP.cfg.linksPath;
        if (!err) {
            obj.switches = data;

            APP.log('[INFO] Loading ... ' + nextUrl);
            d3.json(nextUrl, linksLoaded);
        } else {
            APP.log('[ERROR] Failed to load switches.');
        }
    };
    var startLoading = function () {
        var url = APP.cfg.switchesPath;
        APP.log('[INFO] Loading ... ' + url);
        d3.json(url, switchesLoaded);
    };
    startLoading();
};

APP.init = function () {
    this.cfg.queryParams = this.getQueryParams();
    this.view.init();
    this.load();
};
