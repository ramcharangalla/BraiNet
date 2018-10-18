#!/bin/bash
#
# Configure an ec2 instance to act as the remote server

export region=$(aws configure get region)
if [$region = "us-west-2"]; then
  export ami = "ami-e689729e" # Oregon
else
  echo "Only us-west-2 is supported"
  exit 1
fi
export instanceType = "t2.micro"

export name="brain-net"
export cidr="0.0.0.0/0"

hash aws 2>/dev/null
if [ $? -ne 0 ]; then
    echo >&2 "'aws' command line tool required, but not installed.  Aborting."
    exit 1
fi

if [ -z "$(aws configure get aws_access_key_id)" ]; then
    echo "AWS credentials not configured.  Aborting"
    exit 1
fi
