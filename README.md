# Atanasoff-Berry Computer with Modern Technology

## GitHub Action to compile KiCad files

This repo contains an example custom PCB designed in KiCAD 5 with settings appropriate for PCB manufacture and assembly by JLCPCB. It also contains a GitHub workflow that generates the files needed by JLCPCB: gerbers, BOM, & position file. The workflow additionally generates a manual order BOM,  interactive BOM, and PDF export of the schematic. The manual order BOM includes components in the schematic not assembled by JLCPCB, such as board components not available through JLCPCB, enclosure, hardware, etc. The interactive BOM is an html document that assists in identifying component locations on the PCB for manual assembly.

## Setup Repository GitHub Actions Permissions

The workflow requires read/write access to the new repository to execute successfully.

In the new repository navigate to ```Settings```, and open the ```Actions``` > ```General``` page.

In the ```Workflow permissions``` section select ```Read and write permissions```

## New Repository Cleanup

The newly created repository will include the ```Example_Design``` example PCB design from the template repo. Files for this need to be removed and files for the new design need created. This is best done in a local clone of the new repository on your PC.

Using your Git client (Fork, GitHub Desktop, etc.) clone your new repository to a local folder on your PC.

Eventually this file, ```README.md```, should be replaced/updated with details for the new project. Consider keeping the ```Placing Orders``` and ```Failed Workflow Troubleshooting``` sections so instructions for ordering the design and troubleshooting workflow issues are readily available.

Update ```description``` and ```ownership``` entries in ```APM.yml```.

## New KiCAD Project Creation

Open the KiCAD 8 application.

In the KiCAD Project Manager select ```File``` > ```New``` > ```Project```.

In the ```Create New Project``` dialog check the box for ```Create a new directory for the project```. (Missing this step will break the GitHub workflow functionality.)

Navigate to the ```KiCadProjects``` folder under your cloned repository directory, Enter a name for your KiCAD project, and select ```Save```.

## KiCAD Schematic Setup

The GitHub workflow requires additional schematic symbol fields beyond the KiCAD defaults.

In the KiCAD schematic editor (Eeschema) select ```Preferences``` > ```Preferences...```

In the ```Eeschema``` > ```Field Name Templates``` section add the following entries:

- Part No
- Link
- LCSC
- Note

Check the ```URL``` box for the Link field.

This will add the above fields to every new symbol added to the schematic.

Note: These are KiCAD application settings and apply across all projects. If multiple users are contributing to the design, they will each need to apply these settings.

These fields can also be added to all current components in the KiCAD schematic editor under ```Tools``` > ```Edit Symbol Fields...```

For each component in the schematic either LCSC, for parts to be assembled by JLCPCB, or Part No, for parts to not be assembled by JLCPCB, must be filled out. Additionally the fields Link & Note can be filled out to provide additional information in the manually ordered parts BOM for parts to not be assembled by JLCPCB.

## KiCAD PCB Setup

The PCB design needs to be setup to match the capabilities of JLCPCB. These settings can be copied from the ```Example_Design``` in the template repo.

In the KiCAD PCB editor (PCBNew) select ```File``` > ```Board Setup...```

In the bottom left of the ```Board Setup dialog```, select ```Import Settings...```

In the bottom left of the ```Import Settings``` dialog click ```Select All```.

In the ```Import from``` field navigate to the folder ```KiCadProjects\Example_Design``` under your cloned repository directory and select ```Example_Design.pro```.

Select ```Import Settings```.

## Setup For Multiple Boards

Projects with more than one PCB, for example a motherboard and one or more daughter boards are supported. Follow the steps under ```New KiCAD Project Creation```, ```KiCAD Schematic Setup```, & ```KiCAD PCB Setup``` to create and setup a separate KiCad project for each board.

## Remove Example Design KiCad Project

After setting up the new KiCad project(s), the Example_Design files are no longer needed and the ```KiCadProjects\Example_Design``` folder can be deleted.

## Selecting Parts For JLCPCB Assembly

JLCPCB's parts catalog can be accessed at [jlcpcb.com/parts](https://jlcpcb.com/parts).

Prior to selecting parts for JLCPCB assembly it is recommended to review JLCPCB's PCB Assembly FAQs [Part 1](https://jlcpcb.com/help/article/98-pcb-assembly-faqs) & [Part 2](https://jlcpcb.com/help/article/39-pcb-assembly-faqs-part-2). Pay specific attention to the section related to Basic vs. Extended parts and the section related to polarity of components.

Recommended part selection priorities should be:

- Only SMD parts if possible (Avoid hand assembly & hand soldering fees)
- Prefer Basic parts vs. Extended parts (Avoid additional setup fees)
- Prefer parts with large inventories ( Avoid pre-order process. See ```NOTE``` under ```Placing Orders``` )

Once a part is selected enter the JLCPCB Part # in the LCSC field of the component in the KiCAD schematic.

## Notes on Footprint Rotation

JLCPCB uses the orientation of the component in the tape & reel when the feed direction is up as the zero degree reference. ( See JLCPCB's PCB Assembly FAQ [Part 2](https://jlcpcb.com/help/article/39-pcb-assembly-faqs-part-2). ) KiCAD uses the footprint orientation as the zero degree reference. For many standard component packages, common tape & reel orientations do not match the standard KiCAD footprint orientations. However, the position file generation tool of the GitHub workflow applies corrections for several standard packages to address this. ( See the internal list of rotations at the end of the [Notes about the position file](https://github.com/INTI-CMNB/KiBot#notes-about-the-position-file) section in the KiBot documentation. )

Consider an example design using an SOIC-8 component oriented with pin 1 at the top right. JLCPCB's PCB Assembly FAQ [Part 2](https://jlcpcb.com/help/article/39-pcb-assembly-faqs-part-2) indicates that for On Semiconductors SOIC-8 packages JLCPCB's zero orientation has pin 1 at the bottom-left, which would require a rotation of 180° to put pin 1 at the top-right.

KiCAD's default SOIC-8 footprint, ```SOIC-8_3.9x4.9mm_P1.27mm```, has pin 1 at the top-left. The footprint would be rotated 270° to put pin 1 at the top-right.

The footprint name, ```SOIC-8_3.9x4.9mm_P1.27mm```, matches the ```^SOIC-``` line in the internal list of rotations which adds 270° to the KiCAD rotation. This results in a total rotation of 180° in the position file, which matches the rotation expected by JLCPCB.

## Placing Orders

Prior to preparing to place orders, be sure all design changes are committed and pushed to the GitHub repository.

To generate files required for ordering parts navigate to the ```Issues``` page of the Github repository, and select ```New issue```.

To the right of ```Order and Assembly Files Request``` select ```Get started```.

Replace the ```[Your descriptor here]``` text in the title with a descriptor for your order. The beginning of the title must remain as ```Order and Assembly Files Request:``` in order to be processed.

Enter the quantity of boards you plan to order.

If applicable, select a board variant.

Select ```Submit new issue```.

A new run of the ```Process Order and Assembly Files Request``` workflow will be initiated. After a few minutes the run should complete. If successful the workflow run will add a comment with a link to the workflow run and close the issue. Open the workflow run to access the generated files. The workflow run can also be found under the repository's ```Actions``` page. The workflow run will have the same title as the requesting issue.

At the bottom of the workflow run page is a list of links to the generated files. Selecting ```Manual_Order_and_Assembly_Files``` will download a zip of the manual order BOM, interactive BOM, and schematic. Selecting ```JLCPCB_Files``` will download a zip file of the files needed for the JLCPCB order. These zip files will contain a subfolder for each KiCad project under ```KiCadProjects```.

```NOTE:``` If required parts are not in stock at JLCPCB you will need to use their pre-order process. Before ordering the PCB, parts not in JLCPCB's inventory must be ordered to your personal JLCPB parts library. When ordering these parts the initial price paid is an estimate. The parts will then be quoted. If the quote is less than the estimate the difference will be refunded. If the quote is greater than the estimate a supplemental payment must be made for the difference. The parts will then be purchased and delivered to your parts library at JLCPCB. Once all  parts are in JLCPCB's inventory or your parts library the PCB can be ordered. See [this](https://jlcpcb.com/help/article/49-how-to-build-your-own-parts-library-in-jlcpcb) JLCPCB help article for further details.

## Failed Workflow Troubleshooting

If the workflow run fails, showing a red X icon instead of a green check icon, first verify the request was filled out properly. Second look if the electrical rules check (ERC) and/or design rules check (DRC) failed. These workflow steps perform the same checks as the KiCAD schematic editor (Eescheema) for ERC and PCB editor (PCBNew) for DRC. Run these in the applications and clear all errors before proceeding.

The most common ERC error is a not driven pin error. Pins that have their ```Electrical type``` set as ```Power input``` such as power and ground pins, including the ```GND``` symbol must be connected to a pin with an ```Electrical type``` of ```Power output```. Often the symbol/pin where power & ground are supplied is a generic connector without a ```Power output``` pin. The ```PWR_FLAG``` symbol can be added to the schematic in these locations to clear this error.

## Board Variants

In certain cases it is desirable to have variability within a PCB design. Components may be added, excluded, or substituted as needed to meet various design needs. Variants can be supported through additional fields in the KiCAD schematic and minor modification of the GitHub workflow files.

NOTE: If variants are used by more than one KiCad project in the repo they must be common. When generating order files only one variant can be selected. Any project that does not have the selected variant defined will use the default variant.

### Defining Board Variants

Each board variant is identified by two unique names. One is the name used within KiCAD documents. The other is the name used by the GitHub workflow. These can (but, don't need to) be the same name. Two .yaml files in the repository need modification to define a new variant: ```Config.kibot.yaml``` and ```.github/ISSUE_TEMPLATE/Order_and_Assembly_Files_Request.yaml```.

In ```Config.kibot.yaml``` find the section below

```yaml
variants:
  - name: 'Default'
    comment: 'No variant applied'
    type: kibom
    pre_transform: '_rot_footprint'
```

The ```'Default'``` variant is already defined. This variant does not have any components added, excluded, or substituted. For each new variant add lines similar to below after the ```'Default'``` variant.

```yaml
  - name: 'Low_Spec_Variant'    # Workflow name of variant.
    comment: 'Feature reduced version of design'    # Short description of variant.
    type: kibom    # Do not change this.
    file_id: _Low_Spec    #Suffix included in filename for files generated with this variant.
    variant: Low_Spec    #KiCAD name of variant.
    pre_transform:    # Do not change these.
      - '_var_rename'
      - '_rot_footprint'
```

Insure ```- name``` of the new variant is indented the same as ```- name``` of the default variant and all indentation relative to ```- name``` is maintained as shown here.

Set ```name```, ```comment```, ```file_id```, and ```variant``` to appropriate values. Only ```comment``` may include spaces.

In ```.github/ISSUE_TEMPLATE/Order_and_Assembly_Files_Request.yaml``` find the section below

```yaml
  - type: dropdown
    id: Board_Variant
    attributes:
      label: 'Board_Variant'
      multiple: false
      options:
        - 'Default'
```

For each new variant add a line consisting of '```- [Workflow Name of Variant]```' below and at the same indentation lavel as ```-'Default'``` where ```[Workflow Name of Variant]``` matches the value of ```name``` that was entered in ```Config.kibot.yaml```.

For example, ```- 'Low_Spec_Variant'```

### Adding / Excluding Components In Variants

For any component that is added or excluded from any specific variants, add a field named Config to the symbol in the KiCAD schematic. To add a component to a specific variant enter ```+[KiCAD Name of Variant]``` in the Config field. To remove a component from a specific variant enter ```-[KiCAD Name of Variant]``` in the Config field. If a component is added or removed from multiple variants they can each be added separated by a comma.

For example if R1 should be present in the ```Default``` variant, but not in ```VariantOne``` or ```VariantTwo``` and R2 should be present only in ```VariantTwo``` then the Config field for the two components should be:

|Reference|Config|
|---|---|
|R1|-VariantOne, -VariantTwo|
|R2|+VariantTwo|

### Replacing Components In Variants

To replace a component the default and substituted part(s) must share a common schematic symbol and footprint. The substituted part will have different values for fields like: Value, Part No, LCSC and possibly others. This is accomplished by adding a new field which is named as ```[KiCAD Name of Variant]```:```[Name of Field To Replace]```. A component may have the same or different substituted parts for each variant.

For example if D1 is a green LED with a JLCPCB part # of C2297 and is to be replaced with a red LED with a JLCPCB Part # of C84256 in variant Red_LED, or a yellow LED with a JLCPCB Part # of C2296 in variant Yellow_LED, then the LCSC field would have a value of C2297. Additional fields RED_LED:LCSC with a value of C84256 and Yellow_LED:LCSC with a value of C2296 would be added.

|Reference|LCSC|Red_LED:LCSC|Yellow_LED:LCSC|
|---|---|---|---|
|D1|C2297|C84256|C2296|

## Credits

The GitHub workflow in this repository relies on the KiBot project. [INTI-CMNB/KiBot](https://github.com/INTI-CMNB/KiBot)

## Useful References

KiCAD Tutorial: [Intro to KiCAD - YouTube](https://www.youtube.com/playlist?list=PLEBQazB0HUyR24ckSZ5u05TZHV9khgA1O)

JLCPCB Parts Catalog: [jlcpcb.com/parts](https://jlcpcb.com/parts)

JLCPCB Help Articles: [jlcpcb.com/help/catalog](https://jlcpcb.com/help/catalog)

KiBot GitHub Page [INTI-CMNB/KiBot](https://github.com/INTI-CMNB/KiBot)

easyeda2kicad GitHub Page [easyeda2kicad.py](https://github.com/uPesy/easyeda2kicad.py)
