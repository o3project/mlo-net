
var APP = {
};

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
    svgSize: {width: 708, height: 580},
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
                portIdx = parseInt(ryPortNo) - 2;
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
        for (idxLink = 0; idxLink < ryLinks.length; idxLink += 1) {
            topoLink = ryLinks[idxLink];
            srcRySwIdx = searchNodeIndexByRyDpid(topoLink.src.dpid, nodes);
            dstRySwIdx = searchNodeIndexByRyDpid(topoLink.dst.dpid, nodes);
            if (!srcRySwIdx || srcRySwIdx === dstRySwIdx) {
                continue;
            }

            srcNode = nodes[srcRySwIdx];
            dstNode = nodes[dstRySwIdx];
            srcPortIdx = parseInt(topoLink.src.port_no) - 2;
            dstPortIdx = parseInt(topoLink.dst.port_no) - 2;
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
                APP.log('Multiple nodeToNode: (nodeToNodeKey, count)' 
                        + ' = (' 
                        + nodeToNodeKey + ', ' 
                        + nodeToNodeCounts[nodeToNodeKey] + ')');
            } else {
                nodeToNodeCounts[nodeToNodeKey] = 0;
            }

            link = {
                source: srcRySwIdx,
                target: dstRySwIdx,
                topoLink: topoLink,
                ldBridge: ldBridge,
                nodeToNodeIndex: nodeToNodeCounts[nodeToNodeKey],
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
            };
            objs.push(link);
        }
        return objs;
    }(this.topoLinks, this.nodes, this.ldTopoConf.bridges));

    this.ports = (function (model) {
        var objs = [],
            idxLink = 0,
            link;
        var createPort = function (base, link, main, sub) {
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
    var svgSize = APP.cfg.svgSize;

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

        view.$sliceListPanel.position({
            of: $('#topo-canvas'),
            my: 'left top',
            at: 'left+4 top+4',
            collision: 'none none'
        });
        view.$sliceList.menu({
            items: '> :not(.ui-widget-header)'
        });
        view.$sliceListPanel.find('h3 a').button({
            icons: {primary: 'ui-icon-plus'},
            text: false
        }).click(function () {
            view.$postSliceDialogbox.dialog('open');
        }).position({
            of: view.$sliceListPanel.find('h3:first'),
            my: 'right center',
            at: 'right-2 center',
            collision: 'none none'
        });
        view.$sliceList.find('a').button();
        view.$postSliceDialogbox.dialog({
            autoOpen: false,
            width: 600,
            height: 520,
            modal: true,
            buttons: [ 
                {
                    text: 'Create a slice',
                    click: function () {
                        var sliceObj, 
                            getSliceObj = function () {
                            var slice = {}, 
                                $form = $('form.create-slice');
                            slice.name = $('input[name=slice-name]', $form).val();
                            slice.flows = [];
                            $('fieldset.add-flow-fieldset', $form).each(function (idx, fset) {
                                var $fset, flow = {type:'add'}, sBandwidth, sDelay;
                                $fset = $(fset, $form);
                                flow.name = $('input[name=flow-name]', $fset).val();
                                flow.srcCENodeName = $('input[name=src-ce-node-name]', $fset).val();
                                flow.srcCEPortNo   = $('input[name=src-ce-port-no]', $fset).val();
                                flow.dstCENodeName = $('input[name=dst-ce-node-name]', $fset).val();
                                flow.dstCEPortNo   = $('input[name=dst-ce-port-no]', $fset).val();
                                sBandwidth =  $('input[name=req-bandwidth]', $fset).val();
                                sDelay     =  $('input[name=req-delay]', $fset).val();
                                flow.reqBandWidth = parseInt(sBandwidth);
                                flow.reqDelay = parseInt(sDelay);
                                flow.protectionLevel = '0';
                                slice.flows.push(flow);
                            });
                            return slice;
                        };
                        sliceObj = getSliceObj();
                        APP.log(sliceObj);
                        APP.api.requestToPostSlice({
                            success: function (resJson) {
                                APP.log(resJson);
                                APP.api.requestToGetSlices({
                                    success: function (resJson) {
                                        view.setDataToSliceListPanel(resJson.slices);
                                        view.$postSliceDialogbox.dialog('close');
                                    }
                                });
                            }
                        }, sliceObj);
                    }
                }
            ],
            //show: {effect: 'slice'},
            open: function (event, ui) {
                var $copyEle;
                APP.log('Dialogbox opened. ' + ui);
                $('form>fieldset>fieldset.add-flow-fieldset', $(this)).remove();
                $copyEle = $('.add-flow-fieldset-template .add-flow-fieldset:first').clone();
                $('a.add-flow-button', $(this)).before($copyEle);
            }
        });
        $('a.add-flow-button').button({
            icons: {primary: 'ui-icon-plus'},
            text: 'Add flow'
        }).click(function () {
            var $copyEle = $('.add-flow-fieldset-template .add-flow-fieldset:first').clone();
            $(this).before($copyEle);
            $('input:first', $copyEle).focus();
        });
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
            .attr('x1', function (d) { return d.getSourcePos().x; })
            .attr('y1', function (d) { return d.getSourcePos().y; })
            .attr('x2', function (d) { return d.getTargetPos().x; })
            .attr('y2', function (d) { return d.getTargetPos().y; });

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
            size = APP.cfg.nodeRectSize;

        APP.log('nodes.length = ' + nodes.length);
        that.viewNodes = that.svg.selectAll('.view-node').data(nodes, function (d) { return d.name;});

        g = that.viewNodes.enter().append('g').attr('class', 'view-node');

        g.call(that.force.drag().on('dragstart', function (d) {
            d.fixed = true;
            d3.select(this).classed('fixed', d.fixed);
        }));
        g.on('dblclick', function (d, i) {
            d.fixed = false;
            d3.select(this).classed('fixed', d.fixed);

            if (d.type === 'collapsed-layer') {
                APP.model.removeCollapsedType(d.childrenType);
                APP.model.update();
            } else if (d.type !== 'host') {
                APP.model.addCollapsedType(d.type);
                APP.model.update();
            }
            that.tooltip.style('visibility', 'hidden');
        });
        g.on('mouseover', function () {
            return that.tooltip.style('visibility', 'visible');
        });
        g.on('mousemove', function (d) {
            var ipdisp = '', macdisp = '';
            if (d.ip) {
                ipdisp = '<dt>IP:</dt><dd>' + d.ip + '</dd>';
            }
            if (d.mac) {
                macdisp = '<dt>MAC:</dt><dd>' + d.mac + '</dd>';
            }
            return that.tooltip
                .style('top', (d3.event.pageY - 10) + 'px')
                .style('left', (d3.event.pageX + 10) + 'px')
                .html('<h1>Node</h1>'
                    + '<dl>'
                    + '<dt>Name:</dt><dd>' + d.name + '</dd>'
                    + '<dt>Type:</dt><dd>' + d.type + '</dd>'
                    + '<dt>DP ID:</dt><dd>' + d.ryDpid + '</dd>'
                    + ipdisp
                    + macdisp
                    + '</dl>');
        });
        g.on('mouseout', function () {
            return that.tooltip.style('visibility', 'hidden');
        });
        g.append('image')
            .attr('xlink:href', function (d) {
                var imgRef;
                if ('host' === d.type) {
                    imgRef = './images/host.svg';
                } else if ('edge' === d.type) {
                    imgRef = './images/node-edge-2.svg';
                } else if ('mpls' === d.type) {
                    imgRef = './images/node-mpls.svg';
                } else if ('collapsed-layer' === d.type) {
                    imgRef = './images/layer.svg';
                } else {
                    imgRef = './images/node-any.svg';
                }
                return imgRef;
            })
            .attr('x', 0)
            .attr('y', 0)
            .attr('width', size.width)
            .attr('height', size.height - 8);
        g.append('text')
            .attr('dx', size.width / 2)
            .attr('dy', size.height)
            .attr('text-anchor', 'middle')
            .attr('dominant-baseline', 'alphabetic')
            .text(function (d) { return d.name; });

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
                .html('<h1>Port</h1>'
                    + '<dl>'
                    + '<dt>Name:</dt><dd>' + d.name + '</dd>'
                    + '<dt>Dpid:</dt><dd>' + d.dpid + '</dd>'
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

APP.view.setDataToSliceListPanel = function (slices) {
    var $sliceTitleTpl = this.$sliceListTemplate.find('>.slice-title:first'),
        $flowItemTpl = this.$sliceListTemplate.find('>.flow-item:first'),
        $sliceList = this.$sliceList,
        $sliceTitle;

    $sliceList.find('>*').remove();

    jQuery.each(slices, function (index, slice) {
        var $sliceTitle = $sliceTitleTpl.clone();
        $('.slice-name', $sliceTitle).text(slice.name);
        $('.slice-id', $sliceTitle).text(slice.id);
        $sliceTitle.appendTo($sliceList);
        jQuery.each(slice.flows, function (idxFlow, flow) {
            var $flowItem = $flowItemTpl.clone();
            $flowItem.find('.flow-name').text(flow.name);
            $flowItem.find('.flow-type-name').text(flow.flowTypeName);
            $flowItem.appendTo($sliceList);
        });
    });

    $sliceList.menu('refresh');

    $('.slice-title', $sliceList).each(function () {
        var $that = $(this),
            $delSliceBtn = $('a', $that);
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
};

APP.api = {};

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

APP.api.requestToPostSlice = function (callbacks, sliceObj) {
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
        type: 'POST',
        url: '../slices/',
        cache: false,
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify(reqData),
        success: resCallbacks.success,
        error: resCallbacks.error,
        complete: resCallbacks.complete
    };
    APP.log('Posting slice...');
    $.ajax(settings);
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
            APP.log('[ERROR] Failed to get slices. (textStatus, error) = (' 
                    + textStatus + ', '
                    + error + ')');
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

