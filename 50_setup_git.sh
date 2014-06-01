#!/bin/bash

# setup git
git config --global user.email "dudie.fr+travis+itinerennes+android@gmail.com"
git config --global user.name "Travis CI"

# register Github identity
curl "https://raw.github.com/dudie/maven-repository/master/github_key.pub" >> ~/.ssh/known_hosts
