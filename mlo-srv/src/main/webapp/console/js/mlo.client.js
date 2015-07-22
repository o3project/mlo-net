
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
        thisHref = (window.location.href),
        thisLocPathname = (window.location.pathname);
    APP.log('[INFO] href = ' + thisHref);
    APP.log('[INFO] pathname = ' + thisLocPathname);
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


APP.namespace = function (ns_string) {
    var parts = ns_string.split('.'),
        currObj = APP,
        idx = 0;

    APP.log('[INFO] Defining ... ' + ns_string);

    if (parts[0] === 'APP') {
        parts = parts.slice(1);
    }
    for (idx = 0; idx < parts.length; idx += 1) {
        if (typeof currObj[parts[idx]] === 'undefined') {
            currObj[parts[idx]] = {};
        }
        currObj = currObj[parts[idx]];
    }
    return currObj;
};

APP.namespace('APP.rpc');
APP.rpc = (function (opts) {
    var pfs = {};
    opts = opts || {};

    pfs.userid = 'developer';

    pfs.createResponseCallbacks = function (callbacks) {
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

    pfs.requestWithSliceBody = function (callbacks, url, httpMethod, sliceObj) {
        var userid = pfs.userid,
            settings,
            reqData,
            resCallbacks = pfs.createResponseCallbacks(callbacks);

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

    pfs.requestToGetSlices = function (callbacks) {
        var userid = pfs.userid,
            settings,
            resCallbacks = pfs.createResponseCallbacks(callbacks);

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

    pfs.requestToPostSlice = function (callbacks, sliceObj) {
        var url = '../slices/';
        return pfs.requestWithSliceBody(callbacks, url, 'POST', sliceObj);
    };

    pfs.requestToPutSlice = function (callbacks, sliceObj) {
        var url = '../slices/' + sliceObj.id;
        return pfs.requestWithSliceBody(callbacks, url, 'PUT', sliceObj);
    };

    pfs.requestToDeleteSlice = function (callbacks, sliceid) {
        var userid = pfs.userid,
            settings,
            resCallbacks = pfs.createResponseCallbacks(callbacks);

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

    APP.log('[INFO] APP.rpc');
    return {
        requestToGetSlices: pfs.requestToGetSlices,
        requestToPostSlice: pfs.requestToPostSlice,
        requestToPutSlice: pfs.requestToPutSlice,
        requestToDeleteSlice: pfs.requestToDeleteSlice,
        __END__:null
    };
}());

APP.namespace('APP.model.topology');
APP.model.topology = (function (opts) {
    var pfs = {},
        rpc = APP.rpc;
    opts = opts || {};

    pfs.load = function (cfg) {
        var topoData = {},
            rySwitchesUrl = cfg.switchesPath,
            ryLinksUrl = cfg.linksPath,
            topoConfUrl = cfg.topoConfPath,
            rySwitches,
            ryLinks,
            topoConf,
            loadJson;

        loadJson = function (url, successCb, errorCb) {
            APP.log('[INFO] Loading: ' + url);
            d3.json(url, function (err, data) {
                if (!err) {
                    APP.log('[INFO] Successfully loaded: ' + url);
                    if (successCb) {
                        successCb(data);
                    }
                } else {
                    APP.log('[ERROR] Failed to load: ' + url);
                    APP.log('[ERROR] err: ' + err);
                    if (errorCb) {
                        errorCb(err, data);
                    }
                }
            });
        };

        loadJson(rySwitchesUrl, function (data) {
            rySwitches = data;
            loadJson(ryLinksUrl, function (data) {
                ryLinks = data;
                loadJson(topoConfUrl, function (data) {
                    topoConf = data;
                    pfs.init(rySwitches, ryLinks, topoConf);
                    rpc.requestToGetSlices({
                        success: function (resJson) {
                            APP.view.operation.slices.setDataToSliceListPanel(resJson.slices);
                        }
                    });
                });
            });
        });
    };

    pfs.init = function (switches, links, topoConf) {
        pfs.rySwitches = switches;
        pfs.ryLinks = links;
        pfs.ldTopoConf = topoConf;
        pfs.collapsedTypes = [];

        pfs.update();
    };

    pfs.normalizeLdDpid = function (ldDpid) {
        var splited = ldDpid.split('.', 2),
            prefix = ('0000' + splited[0]).slice(-4),
            seq = splited[1].split(':'),
            idx = 0,
            suffix = '';
        for (idx = 0; idx < seq.length; idx += 1) {
            suffix += ('00' + seq[idx]).slice(-2);
        }
        return (prefix + suffix);
    };

    pfs.searchNodeByRyDpidFromLdTopoConf = function (ryDpid, ldTopoConf) {
        var node = null,
            idx = 0,
            nodes,
            len;

        nodes = ldTopoConf.switches;
        len = nodes.length;
        for (idx = 0; idx < len && !node; idx += 1) {
            if (ryDpid === pfs.normalizeLdDpid(nodes[idx].dpid)) {
                node = nodes[idx];
            }
        }

        nodes = ldTopoConf.hosts;
        len = nodes.length;
        for (idx = 0; idx < len && !node; idx += 1) {
            if (ryDpid === pfs.normalizeLdDpid(nodes[idx].dpid)) {
                node = nodes[idx];
            }
        }
        return node;
    };

    pfs.searchLdBridgeByName = function (ldBrName, ldBridges) {
        var ldBridge,
            idx = 0;
        for (idx = 0; idx < ldBridges.length; idx += 1) {
            if (brName && ldBridges[idx].name === ldBrName) {
                ldBridge = ldBridges[idx];
                break;
            }
        }
        return ldBridge;
    };

    pfs.calcEdgePos = function (mainPos, subPos) {
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

    pfs.constructNodes = function (ldTopoConf, rySwitches, collapsedTypes) {
        var objs = [],
            sw,
            node, collapsedNode,
            idx = 0, 
            collapsedIdx = 0,
            searchCollapsedNodeByType;

        searchCollapsedNodeByType = function (type, nodes) {
            var idx = 0, node;
            for (idx = 0; idx < nodes.length; idx += 1) {
                if (nodes[idx].childrenType && type === nodes[idx].childrenType) {
                    node = nodes[idx];
                    break;
                }
            }
            return node;
        };
        for (idx = 0; idx < rySwitches.length; idx += 1) {
            sw = rySwitches[idx];
            node = pfs.searchNodeByRyDpidFromLdTopoConf(sw.dpid, ldTopoConf);
            if (!node) {
                node = {};
            }
            if (collapsedTypes.indexOf(node.type) > -1) {
                collapsedNode = searchCollapsedNodeByType(node.type, objs);
                if (!collapsedNode) {
                    collapsedNode = {
                        type: 'collapsed-layer', 
                        name: node.type,
                        childrenType: node.type,
                        children: []
                    };
                    objs.push(collapsedNode);
                    objs[collapsedNode.name] = collapsedNode;
                }
                node.topoSwitch = sw;
                node.ryDpid = sw.dpid;
                collapsedNode.children.push(node);
            } else {
                node.topoSwitch = sw;
                node.ryDpid = sw.dpid;
                objs.push(node);
                objs[node.name] = node;
            }
        }
        return objs;
    };

    pfs.searchNodeIndexByRyDpid = function (ryDpid, nodes) {
        var idxNode = 0, idxChild = 0,
            idx = -1,
            node;
        for (idxNode= 0; idxNode < nodes.length; idxNode += 1) {
            node = nodes[idxNode];
            if (ryDpid === node.ryDpid) {
                idx = idxNode;
            } else if (node.children !== undefined) {
                for (idxChild = 0; idxChild < node.children.length; idxChild += 1) {
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

    pfs.searchLdBridge = function (ryDpid, ryPortNo, nodes, ldBridges) {
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

    pfs.constructLink = function (srcRySwIdx, dstRySwIdx, topoLink, ldBridge, nodeToNodeIndex) {
        return ({
            name: ldBridge.name,
            source: srcRySwIdx,
            target: dstRySwIdx,
            topoLink: topoLink,
            ldBridge: ldBridge,
            nodeToNodeIndex: nodeToNodeIndex,
            getSourcePos: function () {
                var edgePos = pfs.calcEdgePos(this.source, this.target),
                    dvec = this.dVec();
                return {x: (edgePos.x + dvec.dx), y: (edgePos.y + dvec.dy)};
            },
            getTargetPos: function () {
                var edgePos = pfs.calcEdgePos(this.target, this.source),
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

    pfs.constructLinks = function (ryLinks, nodes, ldBridges) {
        var objs = [],
            idxLink = 0,
            link,
            srcRySwIdx = 0, dstRySwIdx = 0, srcPortIdx, dstPortIdx,
            srcNode, dstNode,
            ldBridge,
            nodeToNodeKey, nodeToNodeCounts = {}, 
            linkKey, linkKeys = [],
            topoLink;
        for (idxLink = 0; idxLink < ryLinks.length; idxLink += 1) {
            topoLink = ryLinks[idxLink];
            srcRySwIdx = pfs.searchNodeIndexByRyDpid(topoLink.src.dpid, nodes);
            dstRySwIdx = pfs.searchNodeIndexByRyDpid(topoLink.dst.dpid, nodes);
            if (!srcRySwIdx || srcRySwIdx === dstRySwIdx) {
                continue;
            }

            srcNode = nodes[srcRySwIdx];
            dstNode = nodes[dstRySwIdx];
            srcPortIdx = parseInt(topoLink.src.port_no, 10) - 2;
            dstPortIdx = parseInt(topoLink.dst.port_no, 10) - 2;
            ldBridge = pfs.searchLdBridge(topoLink.src.dpid, topoLink.src.port_no, nodes, ldBridges);

            if (srcRySwIdx < dstRySwIdx) {
                nodeToNodeKey = '' + srcNode.name + '-' + dstNode.name;
            } else {
                nodeToNodeKey = '' + dstNode.name + '-' + srcNode.name;
            }
            linkKey = ldBridge.name + '-' + nodeToNodeKey;


            if (linkKeys.indexOf(linkKey) > -1) {
                continue;
            } else {
                linkKeys.push(linkKey);
            }

            if (nodeToNodeCounts[nodeToNodeKey] !== undefined) {
                nodeToNodeCounts[nodeToNodeKey] += 1;
                APP.log('Multiple nodeToNode: (nodeToNodeKey, count)' + 
                        ' = (' + 
                        nodeToNodeKey + ', ' + nodeToNodeCounts[nodeToNodeKey] + ')');
            } else {
                nodeToNodeCounts[nodeToNodeKey] = 0;
            }

            link = pfs.constructLink(srcRySwIdx, dstRySwIdx, topoLink, ldBridge, nodeToNodeCounts[nodeToNodeKey]);
            objs.push(link);
            objs[link.name] = link;
        }
        return objs;
    };

    pfs.constructPorts = function (links) {
        var objs = [],
            idxLink = 0,
            link,
            constructPort;
        constructPort = function (base, link, main, sub) {
            var obj = base;
            obj.link = link;
            obj.getPos = function () {
                var edgePos = pfs.calcEdgePos(link[main], link[sub]),
                    dvec = link.dVec();
                return {x: (edgePos.x + dvec.dx), y: (edgePos.y + dvec.dy)};
            };
            return obj;
        };
        for (idxLink = 0; idxLink < links.length; idxLink += 1) {
            link = links[idxLink];
            objs.push(constructPort(link.topoLink.src, link, 'source', 'target'));
            objs.push(constructPort(link.topoLink.dst, link, 'target', 'source'));
        }
        return objs;
    };

    pfs.update = function () {
        var topologyView = APP.view.topology,
            rySwitches = pfs.rySwitches,
            ryLinks = pfs.ryLinks,
            ldTopoConf = pfs.ldTopoConf,
            newNodes,
            newLinks,
            newPorts;

        newNodes = pfs.constructNodes(ldTopoConf, rySwitches, pfs.collapsedTypes);
        newLinks = pfs.constructLinks(ryLinks, newNodes, ldTopoConf.bridges);
        newPorts = pfs.constructPorts(newLinks);

        topologyView.update({
            nodes: newNodes,
            links: newLinks,
            ports: newPorts,
            __END__: null
        });
    };

    pfs.addCollapsedType = function (type) {
        var types = pfs.collapsedTypes;
        if (types.indexOf(type) < 0) {
            types.push(type);
        }
    };

    pfs.removeCollapsedType = function (type) {
        var types = pfs.collapsedTypes,
            idx = types.indexOf(type);
        if (idx > -1) {
            types.splice(idx, 1);
        }
    };

    pfs.setSelectedFlowTypeName = function (flowTypeName) {
        var topoView = APP.view.topology,
            idx = 0, 
            isFlowTypeNameFound = false, 
            flows;

        APP.log('BEGIN setSelectedFlowTypeName');
        flows = pfs.ldTopoConf.flows;
        for (idx = 0; idx < flows.length; idx += 1) {
            if (flows[idx].name === flowTypeName) {
                topoView.onLinkSelectedWithBrNames(flows[idx].brNames);
                isFlowTypeNameFound = true;
            }
        }
        if (!isFlowTypeNameFound) {
            topoView.onLinkSelectedWithBrNames([]);
        }
        APP.log('END setSelectedFlowTypeName');
    };

    APP.log('[INFO] APP.model.topology');
    return {
        load: pfs.load,
        update: pfs.update,
        addCollapsedType: pfs.addCollapsedType,
        removeCollapsedType: pfs.removeCollapsedType,
        setSelectedFlowTypeName: pfs.setSelectedFlowTypeName,
        __END__: null
    };
}());

APP.namespace('APP.view');

APP.namespace('APP.view.topology');
APP.view.topology = (function (opts) {
    var pfs = {},
        cfg = APP.cfg,
        model = APP.model.topology;
    opts = opts || {};

    pfs.loadSymbolFromSvgImage = function (url, id, parentSelector) {
        d3.xml(url, 'image/svg+xml', function (error, xml) {
            var $xml, $symbol;
            $xml = $(xml);
            $symbol = $('svg symbol', $xml);
            $symbol.attr('id', id).attr('viewBox', $('svg', $xml).attr('viewBox'));
            $(parentSelector).append($symbol);
        });
    };

    pfs.init = function () {
        var svgSize = cfg.svgSize;

        pfs.canvas = d3.select('#topo-canvas');
        pfs.svg = pfs.canvas.append('svg');
        pfs.defs = pfs.svg.append('defs');

        pfs.loadSymbolFromSvgImage('images/host.svg', 'svg-symbol-host', '#topo-canvas svg defs');
        pfs.loadSymbolFromSvgImage('images/node-edge.svg', 'svg-symbol-node-edge', '#topo-canvas svg defs');
        pfs.loadSymbolFromSvgImage('images/node-mpls.svg', 'svg-symbol-node-mpls', '#topo-canvas svg defs');
        pfs.loadSymbolFromSvgImage('images/layer.svg', 'svg-symbol-layer', '#topo-canvas svg defs');
        pfs.loadSymbolFromSvgImage('images/node-any.svg', 'svg-symbol-node-any', '#topo-canvas svg defs');

        pfs.force = d3.layout.force();

        pfs.viewNodes = pfs.svg.selectAll('.view-node');
        pfs.viewLinks = pfs.svg.selectAll('.view-link');
        pfs.viewPorts = pfs.svg.selectAll('.view-port');

        pfs.svg
            .attr('width', svgSize.width)
            .attr('height', svgSize.height);

        pfs.force
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

        pfs.tooltip = pfs.canvas.append('div').attr('class', 'tooltip').text('');

        pfs.xScale = d3.scale.linear()
            .domain([0, svgSize.width])
            .range([0, svgSize.width]);
        pfs.yScale = d3.scale.linear()
            .domain([0, svgSize.height])
            .range([svgSize.height, 0]);

        pfs.xAxis = d3.svg.axis()
            .scale(pfs.xScale)
            .orient('bottom')
            .innerTickSize(-svgSize.height);
        pfs.yAxis = d3.svg.axis()
            .scale(pfs.yScale)
            .orient('left')
            .innerTickSize(-svgSize.width);

        pfs.svg.append('g')
            .attr('class', 'x axis')
            .attr('transform', 'translate(0,' + svgSize.height + ')')
            .call(pfs.xAxis);
        pfs.svg.append('g')
            .attr('class', 'y axis')
            .call(pfs.yAxis);
    };

    pfs.tickLayout = function () {
        var nodeRectSize = cfg.nodeRectSize,
            portRectSize = cfg.portRectSize;

        pfs.viewLinks
            .attr('x1', function (d) { return d.getSourcePos().x; })
            .attr('y1', function (d) { return d.getSourcePos().y; })
            .attr('x2', function (d) { return d.getTargetPos().x; })
            .attr('y2', function (d) { return d.getTargetPos().y; });

        pfs.viewNodes
            .attr('transform', function (d) {
                return ('translate(' + 
                        (d.x - nodeRectSize.width / 2) + ',' + 
                        (d.y - nodeRectSize.height / 2) + ')');
            });

        pfs.viewPorts
            .attr('transform', function (d) {
                var pos = d.getPos(),
                    size = portRectSize;
                return ('translate(' + 
                    (pos.x - size.width / 2) + ',' + 
                    (pos.y - size.height / 2) + ')');
            });
    };

    pfs.showTooltip = function () {
        return pfs.tooltip.style('visibility', 'visible');
    };

    pfs.hideTooltip = function () {
        return pfs.tooltip.style('visibility', 'hidden');
    };

    pfs.copyViewNodeProps = function (nodes) {
        var oldObj,
            newObj,
            sel = pfs.svg.selectAll('.view-node'),
            oldObjs= sel.data(),
            newObjs = nodes,
            idx = 0;
        for (idx = 0; idx < oldObjs.length; idx += 1) {
            oldObj = oldObjs[idx];
            newObj = newObjs[oldObj.name];
            if (newObj) {
                newObj.x = oldObj.x;
                newObj.y = oldObj.y;
                newObj.fixed = oldObj.fixed;
            }
        }
        return newObjs;
    };

    pfs.updateViewNodes = function (nodes) {
        var g,
            size = cfg.nodeRectSize;

        APP.log('nodes.length = ' + nodes.length);
        pfs.viewNodes = pfs.svg.selectAll('.view-node').data(nodes, function (d) { return d.name;});
        
        pfs.viewNodes.classed('alarm', function (d) {return (d.state === 'alarm');});

        g = pfs.viewNodes.enter().append('g');
        g.classed('view-node', true)
            .classed('alarm', function (d) {
                return (d.state === 'alarm');
            });
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
        g.call(pfs.force.drag().on('dragstart', function (d) {
            d.fixed = true;
            d3.select(this).classed('fixed', d.fixed);
        }));
        g.on('dblclick', function (d, i) {
            APP.log('dblclick called.: ' + d.name);
            pfs.hideTooltip();

            d.fixed = false;
            d3.select(this).classed('fixed', d.fixed);

            if (d.type === 'collapsed-layer') {
                model.removeCollapsedType(d.childrenType);
                model.update();
            } else if (d.type !== 'host') {
                model.addCollapsedType(d.type);
                model.update();
            }
        });
        g.on('mouseover', pfs.showTooltip);
        g.on('mouseout',  pfs.hideTooltip);
        g.on('mousemove', function (d) {
            var ipdisp = '', macdisp = '';
            //APP.log('mousemove called.: ' + d.name);
            if (d.ip) {
                ipdisp = '<dt>IP:</dt><dd>' + d.ip + '</dd>';
            }
            if (d.mac) {
                macdisp = '<dt>MAC:</dt><dd>' + d.mac + '</dd>';
            }
            return (pfs.tooltip
                .style('top', (d3.event.pageY - 10) + 'px')
                .style('left', (d3.event.pageX + 10) + 'px')
                .html('<h1>Node</h1>' + 
                    '<dl>' + 
                    '<dt>Name:</dt><dd>' + d.name + '</dd>' + 
                    '<dt>Type:</dt><dd>' + d.type + '</dd>' + 
                    '<dt>DP ID:</dt><dd>' + d.ryDpid + '</dd>' + 
                    ipdisp + 
                    macdisp + 
                    '</dl>'));
        });
        g.on('contextmenu', function (d, i) {
            APP.log('contextmenu called.: ' + d.name);
            d3.event.preventDefault();
            pfs.hideTooltip();
            if (d.type !== 'collapsed-layer') {
                APP.view.operation.remote.openNodeContextMenu(d, d3.event);
            }
        });

        pfs.viewNodes.exit().remove();
    };

    pfs.updateViewLinks = function (links) {
        var lines, g;

        pfs.viewLinks = pfs.viewLinks.data(links);

        lines = pfs.viewLinks.enter().append('line')
            .attr('class', function () { return 'view-link'; });

        g = lines;
        g.on('mouseover', pfs.showTooltip);
        g.on('mouseout',  pfs.hideTooltip);
        g.on('mousemove', function (d) {
            return (pfs.tooltip
                .style('top', (d3.event.pageY - 10) + 'px')
                .style('left', (d3.event.pageX + 10) + 'px')
                .html('<h1>Channel</h1>' + 
                    '<dl>' + '<dt>Name:</dt><dd>' + d.ldBridge.name + '</dd>' + '</dl>'));
        });

        pfs.viewLinks.exit().remove();
    };

    pfs.updateViewPorts = function (ports) {
        var g,
            size = APP.cfg.portRectSize;

        pfs.viewPorts = pfs.viewPorts.data(ports);
        pfs.viewPorts.exit().remove();

        g = pfs.viewPorts.enter().append('g');
        g.attr('class', function () { return 'view-port'; });
        g.append('rect')
            .attr('width', size.width).attr('height', size.height);

        g.on('mouseover', pfs.showTooltip);
        g.on('mouseout',  pfs.hideTooltip);
        g.on('mousemove', function (d) {
            return (pfs.tooltip
                .style('top', (d3.event.pageY - 10) + 'px')
                .style('left', (d3.event.pageX + 10) + 'px')
                .html('<h1>Port</h1>' + 
                    '<dl>' + 
                    '<dt>Name:</dt><dd>' + d.name + '</dd>' + 
                    '<dt>Dpid:</dt><dd>' + d.dpid + '</dd>' + 
                    '</dl>'));
        });
    };

    pfs.update = function (data) {
        var nodes = data.nodes,
            links = data.links,
            ports = data.ports;

        nodes = pfs.copyViewNodeProps(nodes);

        pfs.force
            .on('tick', pfs.tickLayout)
            .nodes(nodes).links(links);
        pfs.force.start();

        pfs.updateViewLinks(links);
        pfs.updateViewNodes(nodes);
        pfs.updateViewPorts(ports);
    };

    pfs.onLinkSelectedWithBrNames = function (brNames) {
        var idx = 0, brName;
        APP.log('onLinkSelectedWithBrNames (' + brNames + ')');
        pfs.viewLinks.classed('view-link-selected', function (d) {
            return (brNames.indexOf(d.ldBridge.name) > -1);
        });
    };

    pfs.setAllLinksToUnselected = function () {
        pfs.onLinkSelectedWithBrNames([]);
    };

    APP.log('[INFO] APP.view.topology');
    return {
        init: pfs.init,
        update: pfs.update,
        onLinkSelectedWithBrNames: pfs.onLinkSelectedWithBrNames,
        setAllLinksToUnselected: pfs.setAllLinksToUnselected,
        __END__: null
    };
}());

APP.namespace('APP.view.operation.slices');
APP.view.operation.slices = (function (opts) {
    var pfs = {},
        cfg = APP.cfg,
        rpc = APP.rpc,
        topologyModel = APP.model.topology;
    opts = opts || {};

    pfs.$sliceListPanel = $('#page-main .slice-list-panel');
    pfs.$postSliceDialogbox = $('#dialogbox-post-slice');
    pfs.$flowOpMenu = $('#page-main .flow-operation-menu');
    pfs.$sliceOpMenu = $('#page-main .slice-operation-menu');

    pfs.getSliceListTemplate = function () {
        return $('.slice-list-template', pfs.sliceListPanel);
    };

    pfs.getSliceList = function () {
        return $('.slice-list', pfs.sliceListPanel);
    };

    pfs.init = function () {
        pfs.initActions();
        pfs.initSliceListPanel(pfs.$sliceListPanel);
        pfs.initSliceOpDialogbox(pfs.$postSliceDialogbox);

        pfs.$flowOpMenu.hide().menu();
        pfs.$sliceOpMenu.hide().menu();
    };

    pfs.initSliceListPanel = function ($sliceListPanel) {
        var $addSliceButton = $('h3 a', $sliceListPanel),
            $sliceList = $('.slice-list', $sliceListPanel);

        // Initializes sliceListPanel
        $sliceListPanel.position({
            of: $('#topo-canvas'),
            my: 'left top',
            at: 'left+4 top',
            collision: 'none none'
        });

        // Initializes sliceList as menu
        $sliceList.menu({
            items: '> :not(.ui-widget-header)'
        });

        // Initializes addSlice button.
        $addSliceButton.button({
            icons: {primary: 'ui-icon-plus'},
            text: false
        }).click(function () {
            var actionKey = 'createSlice',
                doAction = pfs.getAction(actionKey);
            doAction();
        }).position({
            of: $sliceListPanel.find('h3:first'),
            my: 'right center',
            at: 'right-2 center',
            collision: 'none none'
        });
    };

    pfs.initSliceOpDialogbox = function ($sliceOpDialogbox) {
        var $dlg = $sliceOpDialogbox;

        $dlg.dialog({
            autoOpen: false,
            width: 600,
            height: 520,
            modal: true,
            //show: {effect: 'slide', direction: 'up', duration: 250},
            buttons: []
        });

        $dlg.on('dialogopen', function (event, ui) {
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

        $('a.add-flow-button', $dlg).button({
            icons: {primary: 'ui-icon-plus'},
            text: 'Add flow'
        }).click(function () {
            var $copyEle = $('.add-flow-fieldset-template .add-flow-fieldset:first', $dlg).clone();
            $(this).before($copyEle);
            $('input:first', $copyEle).focus();
        });
    };

    pfs.findSliceById = function (sliceid, slices) {
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

    pfs.findFlowIndexById = function (flowid, slice) {
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

    pfs.reloadSlices = function () {
        APP.rpc.requestToGetSlices({
            success: function (resJson) {
                pfs.setDataToSliceListPanel(resJson.slices);
                topologyModel.setSelectedFlowTypeName();
            }
        });
    };

    pfs.createFlowItem = function (flow, $flowItemTpl) {
        var $flowItem = $flowItemTpl.clone();
        $('.flow-id', $flowItem).text(flow.id);
        $('.flow-name', $flowItem).text(flow.name);
        $('.flow-type-name', $flowItem).text(flow.flowTypeName);
        return $flowItem;
    };

    pfs.updateSliceOpMenuContent = function ($sliceOpMenu, slice) {
        var $putFlowList    = $('li.put-flow-menu-item    ul.operation-flow-list', $sliceOpMenu),
            $deleteFlowList = $('li.delete-flow-menu-item ul.operation-flow-list', $sliceOpMenu),
            $sliceListTpl = pfs.getSliceListTemplate(),
            $flowItemTpl = $sliceListTpl.find('>.flow-item:first');

        $putFlowList.empty();
        $deleteFlowList.empty();
        jQuery.each(slice.flows, function (idx, flow) {
            var $flowItem = pfs.createFlowItem(flow, $flowItemTpl),
                $newFlowItem;

            $newFlowItem = $flowItem.clone();
            $('>.action-key', $putFlowList.parent()).clone().appendTo($newFlowItem);
            $newFlowItem.appendTo($putFlowList);

            $newFlowItem = $flowItem.clone();
            $('>.action-key', $deleteFlowList.parent()).clone().appendTo($newFlowItem);
            $newFlowItem.clone().appendTo($deleteFlowList);
        });
        $sliceOpMenu.menu('refresh');
    };

    pfs.showSliceOpMenu = function ($sliceOpMenu, sliceid, slices, parentUi) {
        $sliceOpMenu.show().position({
            of: parentUi,
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
            doAction = pfs.getAction(actionKey);
            doAction(sliceid, flowid, slices);
        });
        $(document).one('click', function () {
            $sliceOpMenu.hide();
        });
        return false;
    };

    pfs.setDataToSliceListPanel = function (slices) {
        var $sliceListTpl = pfs.getSliceListTemplate(),
            $sliceTitleTpl = $sliceListTpl.find('>.slice-title:first'),
            $flowItemTpl = $sliceListTpl.find('>.flow-item:first'),
            $sliceList = pfs.getSliceList(),
            $sliceTitle;

        $sliceList.find('>*').remove();

        jQuery.each(slices, function (index, slice) {
            var $sliceTitle = $sliceTitleTpl.clone();
            $('.slice-name', $sliceTitle).text(slice.name);
            $('.slice-id', $sliceTitle).text(slice.id);
            $sliceTitle.appendTo($sliceList);
            jQuery.each(slice.flows, function (idxFlow, flow) {
                var $flowItem = pfs.createFlowItem(flow, $flowItemTpl);
                $flowItem.appendTo($sliceList);
            });
        });

        $sliceList.menu('refresh');

        $('.slice-title', $sliceList).each(function () {
            var $that = $(this),
                $editSliceBtn = $('a.edit-slice-op', $that),
                $sliceOpMenu = pfs.$sliceOpMenu;

            $editSliceBtn.button({
                icons: {primary: 'ui-icon-carat-1-s'},
                text: false
            }).click(function () {
                // Shows slice-editing menu here.
                var sliceid = $('.slice-id', $that).text(),
                    slice = pfs.findSliceById(sliceid, slices);

                pfs.updateSliceOpMenuContent($sliceOpMenu, slice);
                pfs.showSliceOpMenu($sliceOpMenu, sliceid, slices, this);

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
                    topologyModel.setSelectedFlowTypeName(flowTypeName);
                });
            }
        });
    };

    pfs.getSliceObjFromSliceDlg = function (flowType) {
        var slice = {},
            $sliceDlg = pfs.$postSliceDialogbox,
            $form = $('form.create-slice', $sliceDlg);

        slice.name = $('input[name=slice-name]', $form).val();
        slice.flows = [];
        $('fieldset.add-flow-fieldset', $form).each(function (idx, fset) {
            var $fset, 
                flow = {type:flowType}, 
                sBandwidth, 
                sDelay;

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

    pfs.initActions = function () {
        pfs.actions = {
            createSlice: pfs.doCreateSliceAction,
            deleteSlice: pfs.doDeleteSliceAction,
            addFlow: pfs.doAddFlowAction,
            modifyFlow: pfs.doModifyFlowAction,
            deleteFlow: pfs.doDeleteFlowAction,
            __END__: null
        };
    };

    pfs.getAction = function (actionKey) {
        return pfs.actions[actionKey];
    };

    pfs.doCreateSliceAction = function (sliceid, flowid, slices) {
        var $sliceDlg = pfs.$postSliceDialogbox,
            sliceObj,
            buttonClicked;
        buttonClicked = function () {
            sliceObj = pfs.getSliceObjFromSliceDlg('add');
            rpc.requestToPostSlice({
                success: function (resJson) {
                    APP.log(resJson);
                    pfs.reloadSlices();
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
    };
    
    pfs.doDeleteSliceAction = function (sliceid, flowid, slices) {
        rpc.requestToDeleteSlice({
            success: function (resJson) {
                APP.log(resJson);
                pfs.reloadSlices();
            }
        }, sliceid);
    };

    pfs.doAddFlowAction = function (sliceid, flowid, slices) {
        var $sliceDlg = pfs.$postSliceDialogbox,
            originalSlice = pfs.findSliceById(sliceid, slices),
            sliceObj,
            buttonClicked;
        buttonClicked = function () {
            sliceObj = pfs.getSliceObjFromSliceDlg('add');
            sliceObj.id = sliceid;
            rpc.requestToPutSlice({
                success: function (resJson) {
                    APP.log(resJson);
                    pfs.reloadSlices();
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
    };

    pfs.doModifyFlowAction = function (sliceid, flowid, slices) {
        var $sliceDlg = pfs.$postSliceDialogbox,
            originalSlice = pfs.findSliceById(sliceid, slices),
            flowIndex = pfs.findFlowIndexById(flowid, originalSlice),
            sliceObj,
            buttonClicked;
        buttonClicked = function () {
            sliceObj = pfs.getSliceObjFromSliceDlg('mod');
            sliceObj.id = sliceid;
            delete sliceObj.name;
            if (sliceObj.flows.length === 1) {
                sliceObj.flows[0].id = flowid;
                rpc.requestToPutSlice({
                    success: function (resJson) {
                        APP.log(resJson);
                        pfs.reloadSlices();
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
    };

    pfs.doDeleteFlowAction = function (sliceid, flowid, slices) {
        var $sliceDlg = pfs.$postSliceDialogbox,
            originalSlice = pfs.findSliceById(sliceid, slices),
            flowIndex = pfs.findFlowIndexById(flowid, originalSlice),
            flow,
            sliceObj;

        if (flowIndex > -1) {
            flow = originalSlice.flows[flowIndex];
            sliceObj = {
                id: sliceid,
                flows: [{ type: 'del', id: flowid }]
            };
            rpc.requestToPutSlice({
                success: function (resJson) {
                    APP.log(resJson);
                    pfs.reloadSlices();
                }
            }, sliceObj);
        } else {
            APP.log('[ERROR] Failed to delete flow. Invalid flow id: ' + 
                    '(sliceid, flowid) = (' + sliceid + ', ' + flowid + ')');
        }
    };

    APP.log('[INFO] APP.view.operation.slices');
    return {
        init: pfs.init,
        setDataToSliceListPanel: pfs.setDataToSliceListPanel,
        __END__: null
    };
}());

APP.namespace('APP.view.operation.remote');
APP.view.operation.remote = (function (opts) {
    var pfs = {},
        topologyModel = APP.model.topology;
    opts = opts || {};

    pfs.$nodeContextMenu = $('#page-main .node-context-menu');
    pfs.$nodeTermDlg = $('#dialog-node-terminal');

    pfs.init = function () {
        pfs.$nodeContextMenu.hide().menu();

        pfs.initNodeTerminalDialog(pfs.$nodeTermDlg);
    };

    pfs.openNodeContextMenu = function (nodeData, evt) {
        var $nodeCntxtMenu = pfs.$nodeContextMenu;

        APP.log('Opening node context menu on ' + nodeData.name);
        $nodeCntxtMenu.menu('refresh');
        $nodeCntxtMenu.show().position({
            of: evt,
            my: 'left top',
            at: 'center center',
            collision: 'none none'
        }).one('menuselect', function (event, ui) {
            var $item = $(ui.item[0]),
                actionKey = $('>.action-key', $item).text();

            APP.log('node context menu selected. actionKey = ' + actionKey);
            if (actionKey === 'connectToNode') {
                pfs.openNodeAccessDialog(nodeData.name);
            }
        });
        $(document).one('click', function () {
            $nodeCntxtMenu.hide();
        });
    };

    pfs.openNodeAccessDialog = function (nodeName) {
        if ((typeof mloApi !== 'undefined') && mloApi.openNodeAccessDialogbox) {
            mloApi.openNodeAccessDialogbox(nodeName);
        } else {
            try {
                pfs.openNodeTerminal(nodeName);
            } catch (e) {
                APP.log('Failed to open node access view. e = ' + e);
                alert('This operation is not supported yet.');
            }
        }
    };

    pfs.initNodeTerminalDialog = function ($termDlg) {
        var $execCmdBtn = $('.exec-cmd-button', $termDlg),
            $termInTxtf = $('.term-in', $termDlg),
            $termOutTxta = $('.term-out', $termDlg),
            $statusLbl = $('.status-bar span', $termDlg);
        
        $termDlg.dialog({
            modal: false,
            autoOpen: false,
            width: 600,
            height: 400,
            title: 'Node terminal',
            buttons: [],
            open: function (event) {
                $termInTxtf.attr('disabled', 'disabled');
                $termOutTxta.val('');
                $statusLbl.text('Connecting ... ');
            }
        });
        
        $termDlg.on('dialogbeforeclose', function (event, ui) {
            APP.log('termDlg dialogbeforeclose called.');
            pfs.webSocket.close();
        });

        $execCmdBtn.button({
            icons: { primary: 'ui-icon-arrowthick-1-w' },
            text: null
        }).click(function () {
            var msg;
            if ($termInTxtf.is(':disabled') === false) {
                msg = $termInTxtf.val();
                pfs.sendMessage(pfs.webSocket, msg);
            }
            return false;
        });

        $termInTxtf.keypress(function (event) {
            var msg;
            if (event.which === 13) {
                msg = $termInTxtf.val();
                pfs.sendMessage(pfs.webSocket, msg);
                return false;
            }
        });
    };

    pfs.connectWs = function (wsUri, onMessageCallback) {
        var ws,
            $termInTxtf = $('.term-in', '#dialog-node-terminal'),
            $statusLbl = $('.status-bar span', '#dialog-node-terminal');
        
        APP.log('Creating web socket ...');
        try {
            ws = new WebSocket(wsUri);
            ws.onopen = function (event) {
                APP.log('web socket opened.');
            };
            ws.onmessage = function (event) {
                var alarmResponseDto = JSON.parse(event.data);
                if (alarmResponseDto.status === 'ok') {
                    $termInTxtf.removeAttr('disabled');
                    $termInTxtf.val('');
                    $statusLbl.removeClass('error');
                    $statusLbl.html('Connected.');
                    onMessageCallback(alarmResponseDto.result);
                } else if (alarmResponseDto.status === 'ng') {
                    ws.close();
                    $termInTxtf.attr('disabled', 'disabled');
                    $statusLbl.addClass('error');
                    $statusLbl.html('ERROR: ' + alarmResponseDto.exception);
                    APP.log(alarmResponseDto.exception);
                }
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

    pfs.sendMessage = function (ws, msg) {
        var remoteAccessRequestDto = {
            commandString : msg
        };
        APP.log('execCmdBtn clicked.');
        ws.send(JSON.stringify(remoteAccessRequestDto));
    };

    pfs.webSocketOnMessageReceived = function (msg) {
        var $termDlg = pfs.$nodeTermDlg,
            $termInTxtf = $('.term-in', $termDlg),
            $termOutTxta = $('.term-out', $termDlg),
            origValue = $termOutTxta.val(),
            newValue = origValue + msg;
        
        $termOutTxta.val(newValue);
        if (newValue.length) {
            $termOutTxta.scrollTop($termOutTxta[0].scrollHeight - $termOutTxta.height());
        }

        $termInTxtf.val('');
        $termInTxtf.focus();
    };

    pfs.openNodeTerminal = function (nodeData) {
        var webSocketUri = ('ws://' + 
            window.location.hostname + ':' + window.location.port + 
            '/DEMO/remote/' + nodeData);
        
        pfs.webSocket = pfs.connectWs(webSocketUri, pfs.webSocketOnMessageReceived);
       
        pfs.$nodeTermDlg.dialog('option', 'title', 'Node terminal: Connecting to ' + nodeData);
        pfs.$nodeTermDlg.dialog('open');
    };

    APP.log('[INFO] APP.view.operation.remote');
    return {
        init: pfs.init,
        openNodeContextMenu: pfs.openNodeContextMenu,
        __END__: null
    };
}());

APP.view.init = function () {
    var topologyView = APP.view.topology,
        sliceOpView = APP.view.operation.slices,
        remoteOpView = APP.view.operation.remote;

    $('a.logout-button').each(function (idx, ele) {
        var href = $(ele).attr('href'),
            pathname = encodeURIComponent(window.location.pathname);
        if (href && href.lastIndexOf('?') > -1) {
            $(ele).attr('href', (href + '&at=' + pathname));
        } else if (href && href.lastIndexOf('?') < 0) {
            $(ele).attr('href', (href + '?at=' + pathname));
        }
    });

    topologyView.init();
    sliceOpView.init();
    remoteOpView.init();
};

APP.load = function () {
    APP.log('[INFO] Loading topology.');
    APP.model.topology.load(APP.cfg);
};

APP.connectToEventsApi = function () {
    var ws, 
        url;

    url = ('ws://' + 
            window.location.hostname + ':' + window.location.port + 
            '/DEMO/events');
    ws = new WebSocket(url);
    ws.onopen = function () {
        APP.log('Events connection is opened.');
    };
    ws.onclose = function () {
        APP.log('Events connection is closed.');
    };
    ws.onmessage = function (message) {
        APP.log(message.data + '\n');
        var obj = JSON.parse(message.data);
        APP.log(obj);
        APP.load();
    };
};

APP.init = function () {
    var ua = window.navigator.userAgent;
    this.cfg.queryParams = this.getQueryParams();
    this.connectToEventsApi();
    this.view.init();
    this.load();
    APP.log('[INFO] user-agent: ' + ua);
};

