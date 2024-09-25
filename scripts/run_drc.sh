#!/bin/bash

# Loop through KiCadProjects sub-folders
for dir in KiCadProjects/*/
do
  dir=${dir%*/}      # remove the trailing "/"
#   echo "::debug::$dir"    # print full directory path to debug
#   echo "::debug::${dir##*/}"    # print sud-directory to debug

  # Change dir to KiCADProject subfolder
  cd $dir

  # Run KiBot DRC check
  [ -f *.kicad_pcb ] && kibot $KICAD_VARIABLES -c ../../Config.kibot.yaml -d ../../Fabrication/${dir##*/} -s update_xml,run_erc -i

  # Return to upper directory
  cd ../../
done
