### Ethernet packet sender/receiver (for Linux raw socket) ###
#
# Public domain software
#
# Coded by Yasusi Kanada
# 2014-7-29

import optparse, socket, time, binascii

BUF_SIZE = 1600		# > 1500

ETH_P_ALL = 3		# To receive all Ethernet protocols

# Interface = "eth0"
Interface = "eth1"

host = socket.gethostbyname(socket.gethostname())


### Packet field access ###

def SMAC(packet):
   return binascii.hexlify(packet[6:12]).decode()

def DMAC(packet):
   return binascii.hexlify(packet[0:6]).decode()

def EtherType(packet):
   return binascii.hexlify(packet[12:14]).decode()

def Payload(packet):
   return binascii.hexlify(packet[14:]).decode()


### Packet handler ###

def printPacket(packet, now, message):
   # print(message, len(packet), "bytes  time:", now,
   #       "\n  SMAC:", SMAC(packet), " DMAC:", DMAC(packet),
   #       " Type:", EtherType(packet), "\n  Payload:", Payload(packet)) # !! Python 3 !!
   print message, len(packet), "bytes time:", now, \
       "\n  SMAC:", SMAC(packet), " DMAC:", DMAC(packet), " Type:", \
       EtherType(packet), "\n  Payload:", Payload(packet) # !! Python 2 !!


def terminal():
   # Parse command line
   parser = optparse.OptionParser()
   parser.add_option("--p", "--port", dest = "port", type="int",
                     help = "Local network port id")
   parser.add_option("--lm", "--lmac", "--localMAC", dest = "lmac", type="str",
                     help = "Local MAC address")
   parser.add_option("--rm", "--rmac", "--remoteMAC", dest = "rmac", type="str",
                     help = "Remote MAC address")
   parser.add_option("--receiveOnly", "--receiveonly",
                     dest = "receiveOnly", action = "store_true")
   # parser.add_option("--promiscuous", dest = "promiscuous", action = "store_true")
   parser.set_defaults(lmac = "ffffffffffff", rmac = "ffffffffffff")
   opts, args = parser.parse_args()

   # Open socket
   sock = socket.socket(socket.AF_PACKET, socket.SOCK_RAW, socket.htons(ETH_P_ALL))
   sock.bind((Interface, ETH_P_ALL))
   sock.setblocking(0)

   # Contents of packet to send (constant)
   sendPacket = binascii.unhexlify(opts.rmac) + binascii.unhexlify(opts.lmac) + \
       b'\x88\xb5' + b'\x00\x01\x02\x03\x04\x05\x06\x07\x08\x09\x0a\x0b\x0c\x0d\x0e\x0f'

   # Repeat sending and receiving packets
   interval = 1
   lastTime = time.time()
   while True:
      now = time.time()

      try:
         packet = sock.recv(BUF_SIZE)
      except socket.error:
         pass
      else:
         dmac = DMAC(packet)
         printPacket(packet, now, "Received:")

      if not opts.receiveOnly:
         if now > lastTime + interval:
            sendBytes = sock.send(sendPacket)
            printPacket(sendPacket, now, "Sent:   ")
            lastTime = now
         else:
            time.sleep(0.001001)
      else:
         time.sleep(0.001001)

terminal()
