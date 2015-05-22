
var APP = APP || {};

$(document).ready(function() {
    APP.init();
});

APP.log = function (msg) {
    if ((typeof console !== 'undefined') && console.log) {
        console.log(msg);
    }
    if ((typeof mloApi !== 'undefined') && mloApi.debug) {
        mloApi.debug(msg);
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
    svgSize: {width: 708, height: 590},
    portRectSize: {width: 8, height: 8},
    nodeRectSize: {width: 32, height: 44},
    linkLabelRectSize: {width: 32, height: 16},
    nodeCircleSize: {r: 20}
};


APP.model = {
    topoSwitches: [],
    topoLinks: [],
    ldTopoConf: {},
    nodes: [],
    links: [],
    ports: [],
    collapsedTypes: []
};

APP.model.init = function (switches, links, topoConf) {
    this.topoSwitches = switches;
    this.topoLinks = links;
    this.ldTopoConf = topoConf;

    this.update();
};

APP.model.update = function () {
    var collapsedTypes = this.collapsedTypes;
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

    var searchLdBridgeByName = function (brName, ldBridges) {
        var ldBridge,
            idx = 0;
        for (idx = 0; idx < ldBridges.length; idx += 1) {
            if (brName && ldBridges[idx].name === brName) {
                ldBridge = ldBridges[idx];
                break;
            }
        }
        return ldBridge;
    };

    var calcEdgePos = function (mainPos, subPos) {
        var nodeExtent = APP.getSizeExtent(APP.cfg.nodeRectSize),
            portExtent = APP.getSizeExtent(APP.cfg.portRectSize),
            lenDx = (mainPos.x - subPos.x),
            lenDy = (mainPos.y - subPos.y),
            linkLen = Math.sqrt(lenDx * lenDx + lenDy * lenDy),
            weight = (1.0 - 0.5 * (nodeExtent + portExtent) / linkLen),
            x0 = mainPos.x * weight,
            y0 = mainPos.y * weight,
            dx = subPos.x * (1.0 - weight),
            dy = subPos.y * (1.0 - weight);
        return {x: (x0 + dx), y: (y0 + dy)};
    };

    this.nodes = (function (model, collapsedTypes) {
        var objs = [],
            sw,
            node, collapsedNode,
            idx = 0, collapsedIdx = 0;
        var searchFirstNodeByType = function (type, nodes) {
            var idx = 0, node;
            for (idx = 0; idx < nodes.length; idx += 1) {
                if (type === nodes[idx].type) {
                    node = nodes[idx];
                    break;
                }
            }
            return node;
        };
        var searchCollapsedNodeByType = function (type, nodes) {
            var idx = 0, node;
            for (idx = 0; idx < nodes.length; idx += 1) {
                if (nodes[idx].childrenType && type === nodes[idx].childrenType) {
                    node = nodes[idx];
                    break;
                }
            }
            return node;
        };
        for (idx = 0; idx < model.topoSwitches.length; idx += 1) {
            sw = model.topoSwitches[idx];
            node = searchNodeFromTopoConf(sw.dpid, model.ldTopoConf);
            if (!node) {
                node = {};
            }
            if (collapsedTypes.indexOf(node.type) > -1) {
                //collapsedNode = searchFirstNodeByType(node.type, objs);
                collapsedNode = searchCollapsedNodeByType(node.type, objs);
                if (!collapsedNode) {
                    collapsedNode = {
                        type: 'collapsed-layer', 
                        name: node.type,
                        childrenType: node.type,
                        children: []
                    };
                    objs.push(collapsedNode);
                }
                node.topoSwitch = sw;
                node.ryDpid = sw.dpid;
                collapsedNode.children.push(node);
            } else {
                node.topoSwitch = sw;
                node.ryDpid = sw.dpid;
                objs.push(node);
            }
        }
        return objs;
    }(this, collapsedTypes));

    this.links = (function (ryLinks, nodes, ldBridges) {
        var objs = [],
            idxLink = 0,
            link,
            srcRySwIdx = 0, dstRySwIdx = 0, srcPortIdx, dstPortIdx,
            srcNode, dstNode,
            ldBridge,
            nodeToNodeKey, nodeToNodeCounts = {}, 
            linkKey, linkKeys = [],
            topoLink;
        var searchNodeIndexByRyDpid = function (ryDpid, nodes) {
            var idxNode = 0, idxChild = 0,
                idx = -1,
                node;
            for (idxNode= 0; idxNode < nodes.length; idxNode += 1) {
                node = nodes[idxNode];
                if (ryDpid === node.ryDpid) {
                    idx = idxNode;
                } else if (node.children !== undefined) {
                    //APP.log('node.children : ' + node.type + ', ' + node.children.length);
                    for (idxChild = 0; idxChild < node.children.length; idxChild += 1) {
                        //APP.log('node.children[idxChild].ryDpid : ' 
                        //        + idxChild + ', ' + node.children[idxChild].ryDpid);
                        if (ryDpid === node.children[idxChild].ryDpid) {
                            idx = idxNode;
                            break;
                        }
                    }
                }
                if (idx > -1) {
                    break;
                }
            }
            return idx;
        };
        var searchLdBridge = function (ryDpid, ryPortNo, nodes, ldBridges) {
            var ldBridge, ldBridgeName,
                idx = 0, idxChild = 0, node,
                portIdx = parseInt(ryPortNo, 10) - 2;
            for (idx = 0; idx < nodes.length; idx += 1) {
                if (ryDpid === nodes[idx].ryDpid) {
                    ldBridgeName = nodes[idx].brNames[portIdx];
                } else if (nodes[idx].children !== undefined) {
                    for (idxChild = 0; idxChild < nodes[idx].children.length; idxChild += 1) {
                        if (ryDpid === nodes[idx].children[idxChild].ryDpid) {
                            ldBridgeName = nodes[idx].children[idxChild].brNames[portIdx];
                            break;
                        }
                    }
                }
                if (ldBridgeName) {
                    break;
                }
            }
            for (idx = 0; idx < ldBridges.length; idx += 1) {
                if (ldBridgeName && ldBridges[idx].name === ldBridgeName) {
                    ldBridge = ldBridges[idx];
                    break;
                }
            }
            return ldBridge;
        };
        var constructLink = function (srcRySwIdx, dstRySwIdx, topoLink, ldBridge, nodeToNodeIndex) {
            return ({
                source: srcRySwIdx,
                target: dstRySwIdx,
                topoLink: topoLink,
                ldBridge: ldBridge,
                nodeToNodeIndex: nodeToNodeIndex,
                getSourcePos: function () {
                    var edgePos = calcEdgePos(this.source, this.target),
                        dvec = this.dVec();
                    return {x: (edgePos.x + dvec.dx), y: (edgePos.y + dvec.dy)};
                },
                getTargetPos: function () {
                    var edgePos = calcEdgePos(this.target, this.source),
                        dvec = this.dVec();
                    return {x: (edgePos.x + dvec.dx), y: (edgePos.y + dvec.dy)};
                },
                dVec: function () {
                    var r,
                        enorm, factor,
                        dfactor = 6.0,
                        dvecx = 0.0, dvecy = 0.0,
                        idx = this.nodeToNodeIndex;
                    if (idx > 0) {
                        r = -1.0 * (this.target.x - this.source.x) / (this.target.y - this.source.y);
                        factor = Math.pow(-1, idx % 2) * (Math.floor(idx / 2) + idx % 2) * dfactor;
                        enorm = Math.sqrt(1.0 + r * r);
                        dvecx = factor * 1.0 / enorm;
                        dvecy = factor * r / enorm;
                    }
                    return {dx: dvecx, dy: dvecy, idx: idx};
                }
            });
        };
        for (idxLink = 0; idxLink < ryLinks.length; idxLink += 1) {
            topoLink = ryLinks[idxLink];
            srcRySwIdx = searchNodeIndexByRyDpid(topoLink.src.dpid, nodes);
            dstRySwIdx = searchNodeIndexByRyDpid(topoLink.dst.dpid, nodes);
            if (!srcRySwIdx || srcRySwIdx === dstRySwIdx) {
                continue;
            }

            srcNode = nodes[srcRySwIdx];
            dstNode = nodes[dstRySwIdx];
            srcPortIdx = parseInt(topoLink.src.port_no, 10) - 2;
            dstPortIdx = parseInt(topoLink.dst.port_no, 10) - 2;
            //ldBridge = searchLdBridgeByName(srcNode.brNames[srcPortIdx], ldBridges);
            ldBridge = searchLdBridge(topoLink.src.dpid, topoLink.src.port_no, nodes, ldBridges);
            //APP.log('ldBridge.name : ' + ldBridge.name);
            
            if (srcRySwIdx < dstRySwIdx) {
                nodeToNodeKey = '' + srcNode.name + '-' + dstNode.name;
            } else {
                nodeToNodeKey = '' + dstNode.name + '-' + srcNode.name;
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
                APP.log('Multiple nodeToNode: (nodeToNodeKey, count)' + 
                        ' = (' + 
                        nodeToNodeKey + ', ' + nodeToNodeCounts[nodeToNodeKey] + ')');
            } else {
                nodeToNodeCounts[nodeToNodeKey] = 0;
            }

            link = constructLink(srcRySwIdx, dstRySwIdx, topoLink, ldBridge, nodeToNodeCounts[nodeToNodeKey]);
            objs.push(link);
        }
        return objs;
    }(this.topoLinks, this.nodes, this.ldTopoConf.bridges));

    this.ports = (function (model) {
        var objs = [],
            idxLink = 0,
            link;
        var constructPort = function (base, link, main, sub) {
            var obj = base;
            obj.link = link;
            obj.getPos = function () {
                var edgePos = calcEdgePos(link[main], link[sub]),
                    dvec = link.dVec();
                return {x: (edgePos.x + dvec.dx), y: (edgePos.y + dvec.dy)};
            };
            return obj;
        };
        for (idxLink = 0; idxLink < model.links.length; idxLink += 1) {
            link = model.links[idxLink];
            objs.push(constructPort(link.topoLink.src, link, 'source', 'target'));
            objs.push(constructPort(link.topoLink.dst, link, 'target', 'source'));
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

APP.model.addCollapsedType = function (type) {
    var types = this.collapsedTypes;
    if (types.indexOf(type) < 0) {
        types.push(type);
    }
};

APP.model.removeCollapsedType = function (type) {
    var types = this.collapsedTypes,
        idx = types.indexOf(type);
    if (idx > -1) {
        types.splice(idx, 1);
    }
};

APP.view = {
    viewNodes: {},
    viewLinks: {},
    viewPorts: {},

    canvas: {},
    svg: {},
    force: {},
    tooltip: {},

    $sliceListPanel: {},
    $flowListPanel: {},
    $flowListTemplate: {}
};

APP.view.init = function () {
    var svgSize = APP.cfg.svgSize,
        loadSymbolFromSvgImage;

    this.canvas = d3.select('#topo-canvas');
    this.svg = this.canvas.append('svg');
    this.svgDefs = this.svg.append('defs');
    
    loadSymbolFromSvgImage = function (url, id, parentSelector) {
        d3.xml(url, 'image/svg+xml', function (error, xml) {
            var $xml, $symbol;
            $xml = $(xml);
            $symbol = $('svg symbol', $xml);
            $symbol.attr('id', id).attr('viewBox', $('svg', $xml).attr('viewBox'));
            $(parentSelector).append($symbol);
        });
    };
    loadSymbolFromSvgImage('images/host.svg', 'svg-symbol-host', '#topo-canvas svg defs');
    loadSymbolFromSvgImage('images/node-edge.svg', 'svg-symbol-node-edge', '#topo-canvas svg defs');
    loadSymbolFromSvgImage('images/node-mpls.svg', 'svg-symbol-node-mpls', '#topo-canvas svg defs');
    loadSymbolFromSvgImage('images/layer.svg', 'svg-symbol-layer', '#topo-canvas svg defs');
    loadSymbolFromSvgImage('images/node-any.svg', 'svg-symbol-node-any', '#topo-canvas svg defs');
    
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

    this.xScale = d3.scale.linear()
        .domain([0, svgSize.width])
        .range([0, svgSize.width]);
    this.yScale = d3.scale.linear()
        .domain([0, svgSize.height])
        .range([svgSize.height, 0]);

    this.xAxis = d3.svg.axis()
        .scale(this.xScale)
        .orient('bottom')
        .innerTickSize(-svgSize.height);
    this.yAxis = d3.svg.axis()
        .scale(this.yScale)
        .orient('left')
        .innerTickSize(-svgSize.width);
    
    this.svg.append('g')
        .attr('class', 'x axis')
        .attr('transform', 'translate(0,' + svgSize.height + ')')
        .call(this.xAxis);
    this.svg.append('g')
        .attr('class', 'y axis')
        .call(this.yAxis);

    this.$flowListPanel = $('.flow-list-panel');
    this.$flowListTemplate = $('.flow-list-template');
    this.$sliceListPanel = $('.slice-list-panel');
    this.$sliceList = $('.slice-list');
    //this.$sliceListTemplate = this.$sliceListPanel.find('>.slice-list-template:first');
    this.$sliceListTemplate = $('.slice-list-template:first', this.$sliceListPanel);
    this.$postSliceDialogbox = $('#dialogbox-post-slice');
    this.$flowOpMenu = $('#page-main .flow-operation-menu');
    this.$sliceOpMenu = $('#page-main .slice-operation-menu');
    this.$nodeContextMenu = $('#page-main .node-context-menu');

    this._initSliceListPanel();

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

        view.$postSliceDialogbox.dialog({
            autoOpen: false,
            width: 600,
            height: 520,
            modal: true,
            //show: {effect: 'slide', direction: 'up', duration: 250},
            buttons: []
        });
        view.$postSliceDialogbox.on('dialogopen', function (event, ui) {
            // default dialogopen event.
            var $copyEle,
                $sliceNameTxtf = $('form>fieldset>input[name=slice-name]', $(this)),
                $addFlowBtn = $('form>fieldset>.add-flow-button', $(this));
            APP.log('default dialogopen called.');

            $sliceNameTxtf.val('');
            $sliceNameTxtf.removeAttr('disabled');

            $addFlowBtn.removeClass('hidden');

            $('form>fieldset>fieldset.add-flow-fieldset', $(this)).remove();
            $copyEle = $('.add-flow-fieldset-template .add-flow-fieldset:first').clone();
            $('a.add-flow-button', $(this)).before($copyEle);
        });

        $('a.add-flow-button').button({
            icons: {primary: 'ui-icon-plus'},
            text: 'Add flow'
        }).click(function () {
            var $copyEle = $('.add-flow-fieldset-template .add-flow-fieldset:first').clone();
            $(this).before($copyEle);
            $('input:first', $copyEle).focus();
        });
        view.$flowOpMenu.hide().menu();
        view.$sliceOpMenu.hide().menu();
        view.$nodeContextMenu.hide().menu();
    }(this));

    this.initNodeTerminalDialog();

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
            .attr('x1', function (d) { return d.getSourcePos().x; })
            .attr('y1', function (d) { return d.getSourcePos().y; })
            .attr('x2', function (d) { return d.getTargetPos().x; })
            .attr('y2', function (d) { return d.getTargetPos().y; });

        that.viewNodes
            .attr('transform', function (d) {
                return ('translate(' + 
                        (d.x - nodeRectSize.width / 2) + ',' + 
                        (d.y - nodeRectSize.height / 2) + ')');
            });

        that.viewPorts
            .attr('transform', function (d) {
                var pos = d.getPos(),
                    size = APP.cfg.portRectSize;
                return ('translate(' + 
                    (pos.x - size.width / 2) + ',' + 
                    (pos.y - size.height / 2) + ')');
            });
    };

    var updateViewLinks = function () {
        var lines, g;
        that.viewLinks = that.viewLinks.data(links);
        lines = that.viewLinks.enter().append('line')
            .attr('class', function () { return 'view-link'; });

        g = lines;
        g.on('mouseover', function () {
            return that.tooltip.style('visibility', 'visible');
        });
        g.on('mousemove', function (d) {
            return (that.tooltip
                .style('top', (d3.event.pageY - 10) + 'px')
                .style('left', (d3.event.pageX + 10) + 'px')
                .html('<h1>Channel</h1>' + 
                    '<dl>' + '<dt>Name:</dt><dd>' + d.ldBridge.name + '</dd>' + '</dl>'));
        });
        g.on('mouseout', function () {
            return that.tooltip.style('visibility', 'hidden');
        });
        that.viewLinks.exit().remove();
    };

    var updateViewNodes = function () {
        var g,
            size = APP.cfg.nodeRectSize;

        APP.log('nodes.length = ' + nodes.length);
        that.viewNodes = that.svg.selectAll('.view-node').data(nodes, function (d) { return d.name;});

        g = that.viewNodes.enter().append('g').attr('class', 'view-node');

        g.append('text')
            .attr('dx', size.width / 2)
            .attr('dy', size.height)
            .attr('text-anchor', 'middle')
            .attr('dominant-baseline', 'alphabetic')
            .text(function (d) { return d.name; });
        g.append('use')
            .attr('xlink:href', function (d) {
                var xlinkHref;
                if ('host' === d.type) {
                    xlinkHref = '#svg-symbol-host';
                } else if ('edge' === d.type) {
                    xlinkHref = '#svg-symbol-node-edge';
                } else if ('mpls' === d.type) {
                    xlinkHref = '#svg-symbol-node-mpls';
                } else if ('collapsed-layer' === d.type) {
                    xlinkHref = '#svg-symbol-layer';
                } else {
                    xlinkHref = '#svg-symbol-node-any';
                }
                return xlinkHref;
            })
            .attr('x', 0)
            .attr('y', 0)
            .attr('width', size.width)
            .attr('height', size.height - 8);

        g.call(that.force.drag().on('dragstart', function (d) {
            d.fixed = true;
            d3.select(this).classed('fixed', d.fixed);
        }));
        g.on('dblclick', function (d, i) {
            APP.log('dblclick called.: ' + d.name);
            that.tooltip.style('visibility', 'hidden');
            
            d.fixed = false;
            d3.select(this).classed('fixed', d.fixed);

            if (d.type === 'collapsed-layer') {
                APP.model.removeCollapsedType(d.childrenType);
                APP.model.update();
            } else if (d.type !== 'host') {
                APP.model.addCollapsedType(d.type);
                APP.model.update();
            }
        });
        g.on('mouseover', function (d) {
            //APP.log('mouseover called.: ' + d.name);
            return that.tooltip.style('visibility', 'visible');
        });
        g.on('mousemove', function (d) {
            var ipdisp = '', macdisp = '';
            //APP.log('mousemove called.: ' + d.name);
            if (d.ip) {
                ipdisp = '<dt>IP:</dt><dd>' + d.ip + '</dd>';
            }
            if (d.mac) {
                macdisp = '<dt>MAC:</dt><dd>' + d.mac + '</dd>';
            }
            return that.tooltip
                .style('top', (d3.event.pageY - 10) + 'px')
                .style('left', (d3.event.pageX + 10) + 'px')
                .html('<h1>Node</h1>' + 
                    '<dl>' + 
                    '<dt>Name:</dt><dd>' + d.name + '</dd>' + 
                    '<dt>Type:</dt><dd>' + d.type + '</dd>' + 
                    '<dt>DP ID:</dt><dd>' + d.ryDpid + '</dd>' + 
                    ipdisp + 
                    macdisp + 
                    '</dl>');
        });
        g.on('mouseout', function (d) {
            //APP.log('mouseout called.: ' + d.name);
            return that.tooltip.style('visibility', 'hidden');
        });
        g.on('contextmenu', function (d, i) {
            APP.log('contextmenu called.: ' + d.name);
            d3.event.preventDefault();
            that.tooltip.style('visibility', 'hidden');
            if (d.type !== 'collapsed-layer') {
                that.openNodeContextMenu(d, d3.event);
            }
        });

        that.viewNodes.exit().remove();
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
                .html('<h1>Port</h1>' + 
                    '<dl>' + 
                    '<dt>Name:</dt><dd>' + d.name + '</dd>' + 
                    '<dt>Dpid:</dt><dd>' + d.dpid + '</dd>' + 
                    '</dl>');
        });
        g.on('mouseout', function () {
            return that.tooltip.style('visibility', 'hidden');
        });

        g.append('rect')
            .attr('width', size.width).attr('height', size.height);
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

APP.view.findSliceById = function (sliceid, slices) {
    var slice = null,
        idx = 0;
    for (idx = 0; idx < slices.length; idx += 1) {
        if (('' + slices[idx].id) === ('' + sliceid)) {
            slice = slices[idx];
            break;
        }
    }
    return slice;
};

APP.view.findFlowIndexById = function (flowid, slice) {
    var flowIdx = -1,
        idx = 0;
    for (idx = 0; idx < slice.flows.length; idx += 1) {
        if (('' + slice.flows[idx].id) === ('' + flowid)) {
            flowIdx = idx;
            break;
        }
    }
    return flowIdx;
};

APP.view.reloadSlices = function () {
    APP.api.requestToGetSlices({
        success: function (resJson) {
            APP.view.setDataToSliceListPanel(resJson.slices);
        }
    });
};

APP.view.getSliceObjFromSliceDlg = function (flowType) {
    var view = this,
        slice = {},
        $sliceDlg = view.$postSliceDialogbox,
        $form = $('form.create-slice', $sliceDlg);
    
    slice.name = $('input[name=slice-name]', $form).val();
    slice.flows = [];
    $('fieldset.add-flow-fieldset', $form).each(function (idx, fset) {
        var $fset, flow = {type:flowType}, sBandwidth, sDelay;
        $fset = $(fset, $form);
        flow.name = $('input[name=flow-name]', $fset).val();
        flow.srcCENodeName = $('input[name=src-ce-node-name]', $fset).val();
        flow.srcCEPortNo   = $('input[name=src-ce-port-no]', $fset).val();
        flow.dstCENodeName = $('input[name=dst-ce-node-name]', $fset).val();
        flow.dstCEPortNo   = $('input[name=dst-ce-port-no]', $fset).val();
        sBandwidth =  $('input[name=req-bandwidth]', $fset).val();
        sDelay     =  $('input[name=req-delay]', $fset).val();
        flow.reqBandWidth = parseInt(sBandwidth, 10);
        flow.reqDelay = parseInt(sDelay, 10);
        flow.protectionLevel = '0';
        slice.flows.push(flow);
    });
    return slice;
};

APP.view.createAction = function (actionKey, slices) {
    var view = this,
        actions,
        action = null;

    actions = {
        // createSlice-action
        createSlice: function (sliceid, flowid) {
            var $sliceDlg = view.$postSliceDialogbox,
                sliceObj,
                buttonClicked;
            buttonClicked = function () {
                sliceObj = view.getSliceObjFromSliceDlg('add');
                APP.api.requestToPostSlice({
                    success: function (resJson) {
                        APP.log(resJson);
                        view.reloadSlices();
                        $sliceDlg.dialog('close');
                    }
                }, sliceObj);
                return false;
            };
            $sliceDlg.dialog('option', 'title', 'Creating a slice');
            $sliceDlg.dialog('option', 'buttons', [
                {
                    text: 'Create a slice',
                    click: buttonClicked
                }
            ]);
            $sliceDlg.dialog('open');
        },
        // deleteSlice-action
        deleteSlice: function (sliceid, flowid) {
            APP.api.requestToDeleteSlice({
                success: function (resJson) {
                    APP.log(resJson);
                    view.reloadSlices();
                }
            }, sliceid);
        },
        // addFlow-action
        addFlow: function (sliceid, flowid) {
            var $sliceDlg = view.$postSliceDialogbox,
                originalSlice = view.findSliceById(sliceid, slices),
                sliceObj,
                buttonClicked;
            buttonClicked = function () {
                sliceObj = view.getSliceObjFromSliceDlg('add');
                sliceObj.id = sliceid;
                APP.api.requestToPutSlice({
                    success: function (resJson) {
                        APP.log(resJson);
                        view.reloadSlices();
                        $sliceDlg.dialog('close');
                    }
                }, sliceObj);
                return false;
            };
            $sliceDlg.dialog('option', 'title', 'Adding flow(s) to sliceid = ' + sliceid);
            $sliceDlg.dialog('option', 'buttons', [
                {
                    text: 'Update the slice',
                    click: buttonClicked
                }
            ]);
            $sliceDlg.one('dialogopen', function (event, ui) {
                var $sliceNameTxtf =  $('form.create-slice input[name=slice-name]', $sliceDlg);
                APP.log('dialogopen called in adding flow.');
                $sliceNameTxtf.val(originalSlice.name);
                $sliceNameTxtf.attr('disabled', 'disabled');
            });
            $sliceDlg.dialog('open');
        },
        // modifyFlow-action
        modifyFlow: function (sliceid, flowid) {
            var $sliceDlg = view.$postSliceDialogbox,
                originalSlice = view.findSliceById(sliceid, slices),
                flowIndex = view.findFlowIndexById(flowid, originalSlice),
                sliceObj,
                buttonClicked;
            buttonClicked = function () {
                sliceObj = view.getSliceObjFromSliceDlg('mod');
                sliceObj.id = sliceid;
                delete sliceObj.name;
                if (sliceObj.flows.length === 1) {
                    sliceObj.flows[0].id = flowid;
                    APP.api.requestToPutSlice({
                        success: function (resJson) {
                            APP.log(resJson);
                            view.reloadSlices();
                            $sliceDlg.dialog('close');
                        }
                    }, sliceObj);
                } else {
                    APP.log('[ERROR] Failed to modify flow. ' + 
                            'Invalid length of flows: ' + 
                            '(sliceid, flowid) = (' + sliceid + ', ' + flowid + ')');
                }
                return false;
            };
            $sliceDlg.dialog('option', 'title', 
                'Modifying a flow ' + originalSlice.flows[flowIndex].name);
            $sliceDlg.dialog('option', 'buttons', [
                {
                    text: 'Update the slice',
                    click: buttonClicked
                }
            ]);
            $sliceDlg.one('dialogopen', function (event, ui) {
                var $sliceNameTxtf =  $('form.create-slice input[name=slice-name]', $sliceDlg),
                    $flowNameTxtf = $('form.create-slice fieldset.add-flow-fieldset input[name=flow-name]', $sliceDlg),
                    $addFlowButton = $('form.create-slice .add-flow-button', $sliceDlg);
                APP.log('dialogopen called in modifying flow.');
                $sliceNameTxtf.val(originalSlice.name);
                $sliceNameTxtf.attr('disabled', 'disabled');
                $flowNameTxtf.val(originalSlice.flows[flowIndex].name);
                $addFlowButton.addClass('hidden');
            });
            $sliceDlg.dialog('open');
        },
        // deleteFlow-action
        deleteFlow: function (sliceid, flowid) {
            var $sliceDlg = view.$postSliceDialogbox,
                originalSlice = view.findSliceById(sliceid, slices),
                flowIndex = view.findFlowIndexById(flowid, originalSlice),
                flow,
                sliceObj;
            if (flowIndex > -1) {
                flow = originalSlice.flows[flowIndex];
                sliceObj = {
                    id: sliceid,
                    flows: [{ type: 'del', id: flowid }]
                };
                APP.api.requestToPutSlice({
                    success: function (resJson) {
                        APP.log(resJson);
                        view.reloadSlices();
                    }
                }, sliceObj);
            } else {
                APP.log('[ERROR] Failed to delete flow. Invalid flow id: ' + 
                        '(sliceid, flowid) = (' + sliceid + ', ' + flowid + ')');
            }
        }
    };
    return actions[actionKey];
};

APP.view._initSliceListPanel = function () {
    var view = this,
        $addSliceButton = $('h3 a', view.$sliceListPanel);
        $sliceDlg = view.$postSliceDialogbox;

    // Initializes sliceListPanel
    view.$sliceListPanel.position({
        of: $('#topo-canvas'),
        my: 'left top',
        at: 'left+4 top+4',
        collision: 'none none'
    });

    // Initializes sliceList as menu
    view.$sliceList.menu({
        items: '> :not(.ui-widget-header)'
    });

    // Initializes addSlice button.
    $addSliceButton.button({
        icons: {primary: 'ui-icon-plus'},
        text: false
    }).click(function () {
        var actionKey = 'createSlice',
            doAction = view.createAction(actionKey);
        doAction();
    }).position({
        of: view.$sliceListPanel.find('h3:first'),
        my: 'right center',
        at: 'right-2 center',
        collision: 'none none'
    });
};

APP.view.initNodeTerminalDialog = function () {
    var view = this,
        $termDlg = $('#dialog-node-terminal'),
        $execCmdBtn = $('.exec-cmd-button', $termDlg),
        $termInTxtf = $('.term-in', $termDlg),
        $termOutTxta = $('.term-out', $termDlg),
        webSocket,
        connectWs,
        webSocketOnMessageReceived,
        sendMessage;

    connectWs = function (wsUri, onMessageCallback) {
        var ws;
        APP.log('Creating web socket ...');
        try {
            ws = new WebSocket(wsUri);
            ws.onopen = function (event) {
                APP.log('web socket opened.');
            };
            ws.onmessage = function (event) {
                onMessageCallback(event.data);
            };
            ws.onerror = function (event) {
                APP.log('web socket error occurred.');
            };
            ws.onclose = function (event) {
                APP.log('web socket closed.');
            };
            APP.log('web socket created.');
        } catch (e) {
            APP.log('Failed to create web socket. e = ' + e);
            throw e;
        }
        return ws;
    };

    sendMessage = function (ws, msg) {
        APP.log('execCmdBtn clicked.');
        ws.send(msg);
    };

    webSocketOnMessageReceived = function (msg) {
        var origValue = $termOutTxta.val(),
            newValue = origValue + msg;
        $termOutTxta.val(newValue);
        if (newValue.length) {
            $termOutTxta.scrollTop($termOutTxta[0].scrollHeight - $termOutTxta.height());
        }

        $termInTxtf.val('');
        $termInTxtf.focus();
    };

    $termDlg.dialog({
        modal: true,
        autoOpen: false,
        width: 600,
        height: 400,
        title: 'Node terminal',
        buttons: []
    });
    $termDlg.on('dialogbeforeclose', function (event, ui) {
        APP.log('termDlg dialogbeforeclose called.');
        webSocket.close();
    });

    $execCmdBtn.button({
        icons: { primary: 'ui-icon-arrowthick-1-w' },
        text: null
    }).click(function () {
        var msg;
        msg = JSON.stringify($termInTxtf.val() + '\n');
        sendMessage(webSocket, msg);
        return false;
    });

    $termInTxtf.keypress(function (event) {
        var msg;
        if (event.which === 13) {
            msg = JSON.stringify($termInTxtf.val() + '\n');
            sendMessage(webSocket, msg);
            return false;
        }
    });

    view.openNodeTerminal = function (nodeData) {
        var webSocketUri = 'ws://133.108.84.185:9280/ws';
        webSocket = connectWs(webSocketUri, webSocketOnMessageReceived);
        $termDlg.dialog('open');
    };
};

APP.view.setDataToSliceListPanel = function (slices) {
    var that = this,
        view = this,
        $sliceTitleTpl = this.$sliceListTemplate.find('>.slice-title:first'),
        $flowItemTpl = this.$sliceListTemplate.find('>.flow-item:first'),
        $sliceList = this.$sliceList,
        $sliceTitle;

    var createFlowItem = function (flow) {
        var $flowItem = $flowItemTpl.clone();
        $('.flow-id', $flowItem).text(flow.id);
        $('.flow-name', $flowItem).text(flow.name);
        $('.flow-type-name', $flowItem).text(flow.flowTypeName);
        return $flowItem;
    };

    $sliceList.find('>*').remove();

    jQuery.each(slices, function (index, slice) {
        var $sliceTitle = $sliceTitleTpl.clone();
        $('.slice-name', $sliceTitle).text(slice.name);
        $('.slice-id', $sliceTitle).text(slice.id);
        $sliceTitle.appendTo($sliceList);
        jQuery.each(slice.flows, function (idxFlow, flow) {
            var $flowItem = createFlowItem(flow);
            $flowItem.appendTo($sliceList);
        });
    });

    $sliceList.menu('refresh');

    $('.slice-title', $sliceList).each(function () {
        var $that = $(this),
            $delSliceBtn = $('a.delete-slice-op', $that),
            $editSliceBtn = $('a.edit-slice-op', $that);
        $delSliceBtn.button({
            icons: {primary: 'ui-icon-minus'},
            text: false
        }).click(function () {
            // Delete this slice here.
            var sliceid = $('.slice-id', $that).text();
            APP.api.requestToDeleteSlice({
                success: function (resJson) {
                    APP.log(resJson);
                    APP.api.requestToGetSlices({
                        success: function (resJson) {
                            APP.view.setDataToSliceListPanel(resJson.slices);
                        }
                    });
                }
            }, sliceid);
        }).position({
            of: $that,
            my: 'right center',
            at: 'right-34 center',
            collision: 'none none'
        });

        $editSliceBtn.button({
            icons: {primary: 'ui-icon-carat-1-s'},
            text: false
        }).click(function () {
            // Shows slice-editing menu here.
            var sliceid = $('.slice-id', $that).text();
            var updateSliceOpMenu = function ($menu, $flowItems) {
                var $putFlowList    = $('li.put-flow-menu-item    ul.operation-flow-list', $menu),
                    $deleteFlowList = $('li.delete-flow-menu-item ul.operation-flow-list', $menu),
                    slice = view.findSliceById(sliceid, slices);

                $putFlowList.empty();
                $deleteFlowList.empty();
                jQuery.each(slice.flows, function (idx, flow) {
                    var $flowItem = createFlowItem(flow),
                        $newFlowItem,
                        $actionTag;
                    $newFlowItem = $flowItem.clone();
                    $('>.action-key', $putFlowList.parent()).clone().appendTo($newFlowItem);
                    $newFlowItem.appendTo($putFlowList);
                    $newFlowItem = $flowItem.clone();
                    $('>.action-key', $deleteFlowList.parent()).clone().appendTo($newFlowItem);
                    $newFlowItem.clone().appendTo($deleteFlowList);
                });

                $menu.menu('refresh');
            };
            updateSliceOpMenu(view.$sliceOpMenu, $that.next());
            view.$sliceOpMenu.show().position({
                of: this,
                my: 'left top',
                at: 'left bottom',
                collision: 'none none'
            }).one('menuselect', function (event, ui) {
                var $item = $(ui.item[0]),
                    actionKey = $('>.action-key', $item).text(),
                    flowid = 'N/A',
                    doAction;
                if ($item.hasClass('flow-item')) {
                    flowid = $('>.flow-id', $item).text();
                }
                APP.log('(actionKey, sliceid, flowid) = (' + 
                        actionKey + ', ' + sliceid + ', ' + flowid + ')');
                doAction = view.createAction(actionKey, slices);
                doAction(sliceid, flowid);
            });
            $(document).one('click', function () {
                view.$sliceOpMenu.hide();
            });
            APP.log('Flow edit button clicked. ' + this);
            return false;
        }).position({
            of: $that,
            my: 'right center',
            at: 'right-2 center',
            collision: 'none none'
        });
    });

    $sliceList.find('>.flow-item').selectable({
        stop: function () {
            $('.flow-type-name', this).each(function () {
                var flowTypeName = $(this).text();
                APP.log('Flow type name selected : ' + $(this).text());
                APP.model.setSelectedFlowTypeName(flowTypeName);
            });
        }
    });

    $('#page-main > button').button({
        icons: {
            primary: 'ui-icon-gear',
            secondary: 'ui-icon-carat-1-s'
        },
        text: false
    });
};

APP.view.openNodeContextMenu = function (nodeData, evt) {
    var view = this,
        $nodeCntxtMenu = view.$nodeContextMenu;

    APP.log('Opening node context menu on ' + nodeData.name);
    $nodeCntxtMenu.menu('refresh');
    $nodeCntxtMenu.show().position({
        of: evt,
        my: 'left top',
        at: 'center center',
        collision: 'none none'
    }).one('menuselect', function (event, ui) {
        var $item = $(ui.item[0]),
            actionKey = $('>.action-key', $item).text(),
            doAction;
        APP.log('node context menu selected. actionKey = ' + actionKey);
        if (actionKey === 'connectToNode') {
            //view.openNodeTerminal(nodeData);
            APP.api.openNodeAccessDialog(nodeData.name);
        }
    });
    $(document).one('click', function () {
        $nodeCntxtMenu.hide();
    });
};

APP.api = APP.api || {};

APP.api.setFlowListItems = function (sliceName, flowListItems) {
    APP.view.setAllLinksToUnselected();
    APP.view.setFlowListItems(sliceName, flowListItems);
};

APP.api.requestToGetSlices = function (callbacks) {
    var userid = 'developer',
        settings,
        resCallbacks = APP.api.createResponseCallbacks(callbacks);
    
    settings = {
        type: 'GET',
        url: '../slices/?owner=' + userid + '&withFlowList=true',
        cache: false,
        dataType: 'json',
        success: resCallbacks.success,
        error: resCallbacks.error,
        complete: resCallbacks.complete
    };
    APP.log('Getting slices...');
    $.ajax(settings);
};

APP.api._requestWithSliceBody = function (callbacks, url, httpMethod, sliceObj) {
    var userid = 'developer',
        settings,
        reqData,
        resCallbacks = APP.api.createResponseCallbacks(callbacks);

    reqData = {
        common: {
            version: 1,
            srcComponent: {name: userid},
            dstComponent: {name: 'mlo'},
            operation: 'Request'
        },
        slice: sliceObj
    };
    
    settings = {
        type: httpMethod,
        url: url,
        cache: false,
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify(reqData),
        success: resCallbacks.success,
        error: resCallbacks.error,
        complete: resCallbacks.complete
    };
    APP.log('Requesting to ' + httpMethod + ' slice...');
    $.ajax(settings);
};

APP.api.requestToPostSlice = function (callbacks, sliceObj) {
    var url = '../slices/';
    return this._requestWithSliceBody(callbacks, url, 'POST', sliceObj);
};

APP.api.requestToPutSlice = function (callbacks, sliceObj) {
    var url = '../slices/' + sliceObj.id;
    return this._requestWithSliceBody(callbacks, url, 'PUT', sliceObj);
};

APP.api.requestToDeleteSlice = function (callbacks, sliceid) {
    var userid = 'developer',
        settings,
        resCallbacks = APP.api.createResponseCallbacks(callbacks);
    
    settings = {
        type: 'DELETE',
        url: '../slices/' + sliceid + '?owner=' + userid,
        cache: false,
        dataType: 'json',
        success: resCallbacks.success,
        error: resCallbacks.error,
        complete: resCallbacks.complete
    };
    APP.log('Deleting slice...');
    $.ajax(settings);
};

APP.api.createResponseCallbacks = function (callbacks) {
    return {
        success: function (resJson, textStatus, xhr) {
            APP.log(resJson);
            if (!resJson.common.error) {
                if (!!callbacks && !!(callbacks.success)) {
                    callbacks.success(resJson);
                }
            } else {
                APP.log('[ERROR] Error occurs.: ' + resJson.common.error);
                if (!!callbacks && !!(callbacks.apiCallError)) {
                    callbacks.apiCallError(resJson);
                }
            }
        },
        error: function (xhr, textStatus, error) {
            APP.log('[ERROR] Failed to get slices. (textStatus, error) = ' + 
                    '(' + textStatus + ', ' + error + ')');
            if (!!callbacks && !!(callbacks.error)) {
                callbacks.error(textStatus, error);
            }
        },
        complete: function (xhr, textStatus) {
            if (!!callbacks && !!(callbacks.complete)) {
                callbacks.complete(textStatus);
            }
        }
    };
};

APP.api.openNodeAccessDialog = function (nodeName) {
    if ((typeof mloApi !== 'undefined') && mloApi.openNodeAccessDialogbox) {
        mloApi.openNodeAccessDialogbox(nodeName);
    } else {
        APP.log('Failed to open node access view.');
    }
};

APP.load = function () {
    var obj = {};
    var topoConfLoaded = function (err, data) {
        if (!err) {
            obj.topoConf = data;
            APP.model.init(obj.switches, obj.links, obj.topoConf);
            APP.api.requestToGetSlices({
                success: function (resJson) {
                    APP.view.setDataToSliceListPanel(resJson.slices);
                }
            });
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
    $('.busy').activity();
    startLoading();
};

APP.init = function () {
    var ua = window.navigator.userAgent;
    this.cfg.queryParams = this.getQueryParams();
    this.view.init();
    this.load();
    APP.log('user-agent: ' + ua);
};

