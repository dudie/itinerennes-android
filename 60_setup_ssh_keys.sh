#!/bin/bash

# install Travis tools
git clone https://gist.github.com/7630453.git travis-tools

# setup private keys
eval $(ssh-agent)
for id in $SSH_IDS ; do
  cat ${id}.enc | ./travis-tools/decrypt.sh $KEY > $id
  chmod 400 $id
done
