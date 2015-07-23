#!/bin/bash -e 
# Copyright (C) 2015 Hitachi, Ltd.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
# implied.
# See the License for the specific language governing permissions and
# limitations under the License.

TOPOCONF=./topo.conf
. ${TOPOCONF}
BRGFILE=./bridge.conf
. ${BRGFILE}

scan_host() {
    local host
    local i
    local j
    for host in "${SP}" "${EP}"; do
        eval node${N}=${host}
        #echo -n "${host} => "
        eval local brlist=\${${host}_BR:-}
        local br_a=(${brlist})
        for ((i=0; i<${#br_a[@]}; i++)); do
            local br=${br_a[${i}]}
            eval local prtlist=\${${br}_PORTS:-}
            local prt_a=(${prtlist})
            for ((j=0; j<${#prt_a[@]}; j++)); do
                local dp=`echo ${prt_a[${j}]} | cut -d '-' -f1`
		if [ ${dp} != ${host} ]; then
                    #echo -n "${dp} "
                    eval adjacent${N}${i}=${dp}
                fi
            done
        done
        eval adjacent${N}${i}="S"
        N=$((${N}+1))
        if [ ${#br_a[@]} -gt ${M} ]; then
            M=$((${#br_a[@]}+1))
        fi
        #echo
    done
}

scan_switch() {
    local switch
    local i
    local j
    for switch in ${only:-${SWITCHES?}}; do
        eval node${N}=${switch}
        #echo -n "${switch} => "
        eval local brlist=\${${switch}_BR:-}
        local br_a=(${brlist})
        for ((i=0; i<${#br_a[@]}; i++)); do
            local br=${br_a[${i}]}
            eval local prtlist=\${${br}_PORTS:-}
            local prt_a=(${prtlist})
            for ((j=0; j<${#prt_a[@]}; j++)); do
                local dp=`echo ${prt_a[${j}]} | cut -d '-' -f1`
		if [ ${dp} != ${switch} ]; then
                    #echo -n "${dp} "
                    eval adjacent${N}${i}=${dp}
                fi
            done
        done
        eval adjacent${N}${i}="S"
        N=$((${N}+1))
        if [ ${#br_a[@]} -gt ${M} ]; then
            M=$((${#br_a[@]}+1))
        fi
        #echo
    done
}

conv_node() {
    local i
    local match1=0
    local match2=0
    for ((i=0; i<${N}; i++)); do
        #local res=`eval echo node[${i}]=\\$node${i}`
        #echo $res
        local res=`eval echo \\$node${i}`
        if [ "${SP}" = "$res" ]; then
            match1=1
            SP=${i}
        fi
        if [ "${EP}" = "$res" ]; then
            match2=1
            EP=${i}
        fi
    done
    if [ ${match1} -eq 0 ] || [ ${match2} -eq 0 ]; then
        echo "<sp> or <ep> in invalid"
        exit 2
    fi
}

conv_adj() {
    local x
    local i
    local j
    #for ((x=0; x<$((${N}+1)); x++)); do
    for ((x=0; x<${N}; x++)); do
        for ((i=0; i<${M}; i++)); do
            local res1=`eval echo \\$adjacent${x}${i}`
            for ((j=0; j<${N}; j++)); do
                local res2=`eval echo \\$node${j}`
                if [ "$res1" = "$res2" ]; then
                    eval adjacent${x}${i}=${j}
                fi
            done
            local res=`eval echo adj[${x}][${i}]=\\$adjacent${x}${i}`
            #echo $res
        done
    done
}

print_path() {
    local n=$1
    local i
    local j
    local k
    echo -n "\""
    for ((i=0; i<${n}; i++)); do
        local idx=`eval echo \\$path${i}`
        local node=`eval echo \\$node${idx}`
        #if [ $((${i}+1)) -eq ${n} ]; then
        #    echo -n "$node"
        #else
        #    echo -n "$node "
        #
        if [ $((${i}+1)) -lt ${n} ]; then
            local nidx=`eval echo \\$path$((${i}+1))`
            local nnode=`eval echo \\$node${nidx}`
            eval local brlist=\${${node}_BR:-}
            local br_a=(${brlist})
            eval local nbrlist=\${${nnode}_BR:-}
            local nbr_a=(${nbrlist})
            local fmatch=0
            for ((j=0; j<${#br_a[@]}; j++)); do
                local br=${br_a[${j}]}
                for ((k=0; k<${#nbr_a[@]}; k++)); do
                    local nbr=${nbr_a[${k}]}
                    if [ "$br" = "$nbr" ]; then
                        fmatch=1
                        if [ $((${i}+2)) -eq ${n} ]; then
                            echo -n "$br"
                        else
                            echo -n "$br "
                        fi
                        break
                    fi
                done
                [ ${fmatch} -eq 1 ] && break
            done
        fi
    done
    echo "\""
}

dfs() {
    local n=$1
    local goal=$2
    local x=`eval echo \\$path$((${n}-1))`
    local i
    if [ "${x}" = "${goal}" ]; then
        print_path ${n}
    else
        for ((i=0; i<${M}; i++)); do
            local y=`eval echo \\$adjacent${x}${i}`
            if [ "${y}" = "0" ]; then
                break
            fi
            local res=`eval echo \\$visited${y}`
            if [ "$res" != "true" ]; then
                eval path${n}=${y}
                eval visited${y}="true"
                #echo ***dfs called ${n}***
                dfs $((${n}+1)) ${goal}
                eval visited${y}="false"
           fi
        done
    fi
}

SP=$1
EP=$2
if [ "${SP}" ] && [ "${EP}" ] ; then
    N=0
    M=1
    eval node${N}="S"
    eval adjacent${N}${N}="S"
    N=1
    scan_host
    scan_switch
    conv_node
    conv_adj
    #echo N=$N M=$M SP=${SP} EP=${EP}
    i=0
    eval path${i}=${SP}
    eval visited${SP}="true";
    dfs ${SP} ${EP} 
else
    echo "Usage: flow.sh <sp> <ep>"
    exit 2
fi
