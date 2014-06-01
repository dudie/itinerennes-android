#!/bin/bash

# setup git
git config --global user.email "dudie.fr+github@gmail.com"
git config --global user.name "dudie-ci"

# register Github identity
curl "https://raw.github.com/dudie/maven-repository/master/github_key.pub" >> ~/.ssh/known_hosts
