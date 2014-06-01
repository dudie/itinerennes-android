#!/bin/bash

# install Travis tools
git clone https://gist.github.com/7630453.git travis-tools

# setup private keys
eval $(ssh-agent)

echo Setting up SSH ids: $SSH_IDS

for id in $SSH_IDS ; do
  cat ${id}.enc | ./travis-tools/decrypt.sh $KEY > $id
  chmod 400 $id
  ls -l ${id}.enc $id
  ssh-add $id
done
