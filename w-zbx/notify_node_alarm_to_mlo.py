#!/usr/bin/env python
# -*- coding: utf-8 -*-

import sys, os, logging
import json

LOG_DIRPATH = '/var/log/mlo'

def parse_subject(subject_lines):
    pass

def main(argv):
    logging.info(json.dumps(argv))

if __name__ == '__main__':
    log_path = os.path.join(LOG_DIRPATH, ('%s.log' % sys.argv[0]))
    logging.basicConfig(filename=log_path, level=logging.DEBUG)
    main(sys.argv)

