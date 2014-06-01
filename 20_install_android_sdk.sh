#!/bin/bash

# Update system
sudo apt-get update -qq
if [ `uname -m` = x86_64 ] ; then
  sudo apt-get install -qq --force-yes libgd2-xpm ia32-libs ia32-libs-multiarch
fi

# Instance base Android SDK
wget http://dl.google.com/android/android-sdk_r22.3-linux.tgz -O android-sdk-linux.tgz
tar xzf android-sdk-linux.tgz
export ANDROID_HOME="$PWD/android-sdk-linux"
export PATH="${PATH}:${ANDROID_HOME}/tools:${ANDROID_HOME}/platform-tools"

# Install required Android components.
export PKGS="android-$SDK,android-7,sysimg-16,sysimg-19"
echo y | android update sdk \
           --filter build-tools-19.0.0,platform-tools,extra-android-support,$PKGS \
           --no-ui --all --force
