#!/bin/bash

if [ ! -d "temp" ]; then
  echo "Folder /temp is missing, please run build.sh first."
  exit 1
fi

echo "Creating test folder with execution environments."
mkdir -p $PWD/test/Assault
rm -rf $PWD/test/Assault/*
cp temp/dirAssaultParty.zip $PWD/test/Assault
cp temp/dirCollectionSite.zip $PWD/test/Assault
cp temp/dirConcentrationSite.zip $PWD/test/Assault
cp temp/dirMuseum.zip $PWD/test/Assault
cp temp/dirMasterThief.zip $PWD/test/Assault
cp temp/dirOrdinaryThief.zip $PWD/test/Assault

echo "Decompressing execution environments."
cd $PWD/test/Assault
unzip -q dirAssaultParty.zip;       rm -f dirAssaultParty.zip
unzip -q dirCollectionSite.zip;     rm -f dirCollectionSite.zip
unzip -q dirConcentrationSite.zip;  rm -f dirConcentrationSite.zip
unzip -q dirMuseum.zip;             rm -f dirMuseum.zip
unzip -q dirMasterThief.zip;        rm -f dirMasterThief.zip
unzip -q dirOrdinaryThief.zip;      rm -f dirOrdinaryThief.zip