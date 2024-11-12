#!/bin/bash

# Loop through KiCadProjects sub-folders
for dir in KiCadProjects/*/
do
  dir=${dir%*/}      # remove the trailing "/"
#   echo "::debug::$dir"    # print full directory path to debug
#   echo "::debug::${dir##*/}"    # print sud-directory to debug

  # Change dir to KiCADProject subfolder
  cd $dir

  # Run KiBot generate gerbers
  [ -f *.kicad_sch ] && kibot $KICAD_VARIABLES -c ../../Config.kibot.yaml -d ../../Fabrication/${dir##*/} -s run_drc,run_erc zip_gerbers
  # Run KiBot generate JLCPCB BOM
  [ -f *.kicad_sch ] && kibot $KICAD_VARIABLES -c ../../Config.kibot.yaml -d ../../Fabrication/${dir##*/} -s run_drc,run_erc gen_jlc_bom
  # Run KiBot generate position file
  [ -f *.kicad_sch ] && kibot $KICAD_VARIABLES -c ../../Config.kibot.yaml -d ../../Fabrication/${dir##*/} -s run_drc,run_erc position

  # Return to upper directory
  cd ../../
done
