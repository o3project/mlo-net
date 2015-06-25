#!/usr/bin/env python
# -*- coding: utf-8 -*-


import unittest
from notify_node_alarm_to_mlo import *
from datetime import datetime
import json

TEST_DATA_00_PROBLEM = {
        'send_to': '127.0.0.1',
        'subject': 'PROBLEM: Zabbix agent on s5-ldn is unreachable for 5 minutes',
        'message': ('Trigger: Zabbix agent on s5-ldn is unreachable for 5 minutes\n' 
            + 'Trigger status: PROBLEM\n' 
            + 'Trigger severity: Average\n' 
            + 'Trigger URL: \n' 
            + '\n' 
            + 'Item values:\n' 
            + '\n' 
            + '1. Agent ping (s5-ldn:agent.ping): Up (1)\n' 
            + '2. *UNKNOWN* (*UNKNOWN*:*UNKNOWN*): *UNKNOWN*\n' 
            + '3. *UNKNOWN* (*UNKNOWN*:*UNKNOWN*): *UNKNOWN*\n' 
            + '\n' 
            + 'Original event ID: 3250'),
        }

TEST_DATA_00_OK = {
        'send_to': '127.0.0.1',
        'subject': 'OK: Zabbix agent on s5-ldn is unreachable for 5 minutes',
        'message': ('Trigger: Zabbix agent on s5-ldn is unreachable for 5 minutes\n' 
            + 'Trigger status: PROBLEM\n' 
            + 'Trigger severity: Average\n' 
            + 'Trigger URL: \n' 
            + '\n' 
            + 'Item values:\n' 
            + '\n' 
            + '1. Agent ping (s5-ldn:agent.ping): Up (1)\n' 
            + '2. *UNKNOWN* (*UNKNOWN*:*UNKNOWN*): *UNKNOWN*\n' 
            + '3. *UNKNOWN* (*UNKNOWN*:*UNKNOWN*): *UNKNOWN*\n' 
            + '\n' 
            + 'Original event ID: 3250'),
        }

class MloAlarmNotifierTest (unittest.TestCase):
    def setUp(self):
        pass

    def test_parse_subject_problem(self):
        obj = parse_subject(TEST_DATA_00_PROBLEM['subject'])
        self.assertEquals('PROBLEM', obj['triggerStatus'])
        self.assertEquals('Zabbix agent on s5-ldn is unreachable for 5 minutes', obj['triggerName'])

    def test_parse_subject_ok(self):
        obj = parse_subject(TEST_DATA_00_OK['subject'])
        self.assertEquals('OK', obj['triggerStatus'])
        self.assertEquals('Zabbix agent on s5-ldn is unreachable for 5 minutes', obj['triggerName'])

    def test_parse_subject_double_column(self):
        obj = parse_subject('aaa:bbb: ccc: ddd')
        print 'obj = %s' % json.dumps(obj)
        self.assertEquals('aaa:bbb', obj['triggerStatus'])
        self.assertEquals('ccc: ddd', obj['triggerName'])

    def test_create_alarm_obj_problem(self):
        subject_obj = parse_subject(TEST_DATA_00_PROBLEM['subject'])
        alarm_ts = datetime(year=2015, month=6, day=19, 
                hour=12, minute=34, second=56, microsecond=789000, 
                tzinfo=JstTzInfo())
        obj = create_alarm_obj(alarm_ts, subject_obj)
        self.assertIsNotNone(obj)
        self.assertEquals('2015-06-19 12:34:56.789+0900', obj['timestamp'])
        self.assertEquals('alarm', obj['state'])
        self.assertEquals('topology', obj['type'])
        self.assertEquals('node', obj['targetType'])
        self.assertEquals('s5', obj['targetId'])
        self.assertEquals('Zabbix agent on s5-ldn is unreachable for 5 minutes', obj['description'])


if __name__ == '__main__':
    unittest.main()

