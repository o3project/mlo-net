#!/usr/bin/env python
# -*- coding: utf-8 -*-

import sys, os, logging
from datetime import datetime, tzinfo, timedelta
import json
import re
import httplib, urllib

LOG_DIRPATH = '/var/log/mlo'

class JstTzInfo(tzinfo):
    def utcoffset(self, dt):
        return timedelta(hours=9)
    def dst(self, dt):
        return timedelta(0)
    def tzname(self, dt):
        return 'JST'

def parse_subject(subject):
    obj = None
    if ': ' in subject:
        (status, name) = subject.split(': ', 1)
        obj = {
                'triggerStatus': status,
                'triggerName': name,
                }
    return obj

def create_alarm_obj(alarm_timestamp, subject_obj):
    obj = None
    def get_timestamp(ts):
        return (
                ts.strftime('%Y-%m-%d %H:%M:%S')
                + ('.%03d' % (ts.microsecond / 1000))
                + ts.strftime('%z'))
    def get_state(obj):
        state = None
        key = 'triggerStatus'
        if 'PROBLEM' == obj[key]:
            state = 'alarm'
        elif 'OK' == obj[key]:
            state = 'ok'
        return state
    def get_target_id(obj):
        ptn = re.compile('.*\s([^\s]+)-ldn')
        m = ptn.match(obj['triggerName'])
        target_id = None
        if m:
            target_id = m.group(1)
        return target_id
    def get_description(obj):
        return obj['triggerName']
    if subject_obj is not None:
        obj = {
                'timestamp': get_timestamp(alarm_timestamp),
                'state': get_state(subject_obj),
                'type': 'topology',
                'targetType': 'node',
                'targetId': get_target_id(subject_obj),
                'description': get_description(subject_obj),
                }
    return obj

def post_alarm(host, alarm_obj):
    port = '8080'
    path = '/DEMO/alarms/'
    method = 'POST'
    headers = {
            'Accept': 'application/json',
            'Content-type': 'application/json',
            }
    body = json.dumps([alarm_obj])
    conn = httplib.HTTPConnection(host, port)
    res_obj = None
    try:
        conn.request(method, path, body, headers)
        res = conn.getresponse()
        logging.info('(res.status, res.reason) = (%s, %s)' % (res.status, res.reason))
        res_body = res.read()
        logging.info('res body is as follows:')
        logging.info('---- ')
        logging.info(res_body)
        logging.info('---- ')
    finally:
        conn.close()
    return res_obj

def main(argv):
    logging.info(json.dumps(argv))
    host = argv[1]
    subject = argv[2]
    message = argv[3]
    res_body = post_alarm(
            host, 
            create_alarm_obj(
                datetime.now(JstTzInfo()), 
                parse_subject(
                    subject)))

if __name__ == '__main__':
    log_path = os.path.join(LOG_DIRPATH, ('%s.log' % sys.argv[0]))
    logging.basicConfig(filename=log_path, level=logging.DEBUG)
    main(sys.argv)

