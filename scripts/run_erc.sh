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

  # Run KiBot ERC check
  [ -f *.kicad_sch ] && kibot $KICAD_VARIABLES -c ../../Config.kibot.yaml -d ../../Fabrication/${dir##*/} -s update_xml,run_drc -i

  # Return to upper directory
  cd ../../
done
