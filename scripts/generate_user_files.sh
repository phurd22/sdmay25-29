#!/bin/bash

# Loop through KiCadProjects sub-folders
for dir in KiCadProjects/*/
do
  dir=${dir%*/}      # remove the trailing "/"
#   echo "::debug::$dir"    # print full directory path to debug
#   echo "::debug::${dir##*/}"    # print sud-directory to debug

  # Change dir to KiCADProject subfolder
  cd $dir

  # Run KiBot Schematic to PDF
  [ -f *.kicad_sch ] && kibot $KICAD_VARIABLES -c ../../Config.kibot.yaml -d ../../Fabrication/${dir##*/} -s run_drc,run_erc sch_to_pdf
  # Run KiBot Manual Order BOM
  [ -f *.kicad_sch ] && kibot $KICAD_VARIABLES -c ../../Config.kibot.yaml -d ../../Fabrication/${dir##*/} -s run_drc,run_erc man_order_html_bom
  # Run KiBot Integrated BOM
  [ -f *.kicad_sch ] && kibot $KICAD_VARIABLES -c ../../Config.kibot.yaml -d ../../Fabrication/${dir##*/} -s run_drc,run_erc gen_int_bom

  # Return to upper directory
  cd ../../
done
