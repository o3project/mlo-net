#!/bin/sh
# delete all entries of the switch
# POST /stats/flowentry/clear/<dpid>

. ./topo.conf

dpid2decimal() {
    local macaddr=`echo $1 | cut -d '.' -f2`
    local str=
    local IFS=:
    for byte in ${macaddr}; do
        str="${str}${byte}"
    done
    echo "$(printf '%d' 0x${str})"
}

eval dpid=\${$1_DPID:-}
if [ "${dpid}" ]; then
    dpid_n=$(dpid2decimal ${dpid})
    curl -s -X DELETE http://REST:PORT/stats/flowentry/clear/${dpid_n}
else
    echo "Usage: clear_flow.sh <dpid>"
    exit 2
fi
