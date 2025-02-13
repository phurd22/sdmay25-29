#!/bin/bash

# Loop through KiCadProjects sub-folders
for dir in KiCadProjects/*/
do
  dir=${dir%*/}      # remove the trailing "/"
#   echo "::debug::$dir"    # print full directory path to debug
#   echo "::debug::${dir##*/}"    # print sud-directory to debug

  # Change dir to KiCADProject subfolder
  cd $dir

  pwd

  # Run KiBot DRC check
  [ -f *.kicad_pcb ] && kibot $KICAD_VARIABLES --plot-config ../../Config.kibot.yaml --out-dir ../../Fabrication/${dir##*/} --skip-pre update_xml,erc --invert-sel

  # Return to upper directory
  cd ../../
done
