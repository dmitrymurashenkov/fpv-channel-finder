# fpv-channel-finder

Channel calculator for FPV drones to find channels with enough separation.

## How to use

No GUI exists - it's a simple Java app that should be compiled and run. 

In order to change params - modify them in the source code and run.

## Good channel set criteria

There are several reasons not every channel can used when multiple pilots fly simultaneously:

- Channel separation - if two channels are close together then video receiver has hard time distinguishing between 
two signals, especially since no VTX are perfect and they all produce signal spread

- Intermodulation distortion (IMD) - when two VTX work simultaneously on frequencies F1 and F2 there would also be 
2 additional peaks on `F1*2 - F2` and `F2*2 - F1` frequencies

-  Harmonics - 5.8Ghz VTX produce harmonics on F + 190Mhz and F + 240Mhz (can't find reason for this - but they can 
be seen during spectrum scan)

In order to fly comfortable we must choose channels that are far away from all these peaks. 

This app simply searches for channel sets that satisfy specified separation parameters.

Note that largest peak is usually from other channels, the IMD peaks are smaller and harmonics are even smaller, this 
should be taken into account when choosing separation gaps (harmonics gap can be set to zero for example if everybody 
has good vtx). Although on some VTX we've seen quite large harmonics peaks - you could almost see another pilot's video 
on a higher channel. 

## Legal and locked channels

In some countries channels E4, E7 and E8 are illegal to use and are locked in some VTX.

In order to use them in Betaflight - you have to mark E band (or any band) as CUSTOM instead of FACTORY. This will make 
FC send command to VTX to set specified frequency (any arbitrary frequency can be set) and not command to set channel 
predefined in VTX.

Some VTX are hardware locked and have to be unlocked to support CUSTOM bands.

## Results

For whoop-style 25mW VTX in confined space separation should be at least:

- 25Mhz between channel and any other channel
- 10Mhz between channel and any harmonic peak 
- 10Mhz between channel and any IMD peak

But better to have 25/15/15 (channel, harmonic, IMD) separation.

For a top quality VTX you can get away with values about 25/7/7.

### Best channel sets - no locked channels (E4 E7 E8)

**7 pilots 26/11/11**

`R7 R8 F2 F4 E1 E3 B6`

`R8 F2 F4 F8 E1 E3 B6`

**6 pilots 25/17/17**

`R1 R7 R8 F4 B2 A3`

`R1 R8 F4 F8 B2 A3`

**5 pilots 30/17/26**

`R1 R2 R8 F5 B3`

### Best channel sets - including locked channels (E4 E7 E8)

**7 pilots 26/11/11**

`R7 R8 F2 F4 E1 E3 B6`

`R8 F2 F4 F8 E1 E3 B6`

**6 pilots 25/19/19**

`R2 F7 E6 E4 B4 A6`

`R2 E6 E4 B4 A6 A1`

**5 pilots 46/17/27**

`R1 F5 E8 B8 A8`


### Backup channel sets - no locked channels (E4 E7 E8) 

These are for the case when there is some other signal source (for example, wifi) occupying one of the channels used 
in the best sets.

Note that same frequency can be known under several channel names - so avoid close channels also!

![](frequency-bands.png)

**6 pilots 25/12/14**

`R1 R2 R4 R8 B6 A1`

`R1 R2 R8 F3 B2 A3`

`R1 R2 R8 B3 B6 A1`

`R1 R7 R8 F3 F5 E1`

`R1 R7 R8 F4 E1 B6`

`R1 R7 R8 F4 E1 A3`

`R1 R7 R8 F4 B2 A3`

`R1 R8 F1 F3 E5 A4`

`R1 R8 F3 F5 F8 E1`

`R1 R8 F4 F8 E1 B6`

`R1 R8 F4 F8 E1 A3`

`R1 R8 F4 F8 B2 A3`

`R1 R8 F4 E1 B3 B8`

`R2 R8 F1 F5 F7 A6`

`R2 F2 F6 E6 B4 A1`

`R7 F2 F6 E1 E3 A5`

`F2 F6 F8 E1 E3 A5`

**6 pilots 25/10/12**


`R1 R2 R4 R8 F7 B6`

`R1 R2 R4 R8 F7 A3`

`R1 R2 R4 R8 B6 B8`

`R1 R2 R4 R8 B6 A1`

`R1 R2 R8 F3 B2 B6`

`R1 R2 R8 F3 B2 A3`

`R1 R2 R8 F7 B2 B6`

`R1 R2 R8 F7 B2 A3`

`R1 R2 R8 F7 B3 B6`

`R1 R2 R8 F7 B3 A7`

`R1 R2 R8 F7 B3 A3`

`R1 R2 R8 B2 B6 B8`

`R1 R2 R8 B2 B6 A1`

`R1 R2 R8 B3 B6 B8`

`R1 R2 R8 B3 B6 A1`

`R1 R2 R8 B3 B8 A7`

`R1 R2 R8 B3 A7 A1`

`R1 R3 R7 F5 E1 A5`

`R1 R3 R7 E1 A5 A3`

`R1 R3 F5 F8 E1 A5`

`R1 R3 F8 E1 A5 A3`

`R1 R4 R8 F4 E1 B8`

`R1 R4 R8 F4 E1 A1`

`R1 R5 R8 F1 F3 E5`

`R1 R5 R8 F1 E5 A6`

`R1 R7 R8 F3 F5 E1`

`R1 R7 R8 F4 E1 B6`

`R1 R7 R8 F4 E1 A3`

`R1 R7 R8 F4 B2 B6`

`R1 R7 R8 F4 B2 A3`

`R1 R8 F1 F3 E5 A4`

`R1 R8 F1 E5 A6 A4`

`R1 R8 F3 F5 F8 E1`

`R1 R8 F3 F7 B2 B6`

`R1 R8 F3 E5 B2 A3`

`R1 R8 F4 F8 E1 B6`

`R1 R8 F4 F8 E1 A3`

`R1 R8 F4 F8 B2 B6`

`R1 R8 F4 F8 B2 A3`

`R1 R8 F4 E1 B3 B8`

`R1 R8 F4 E1 B3 A1`

`R1 R8 F4 E5 B2 B6`

`R1 R8 F4 E5 B2 A3`

`R2 R8 F1 F5 F7 A6`

`R2 R8 F1 F6 E3 B8`

`R2 R8 F3 F6 E3 A7`

`R2 R8 F3 E3 B2 A3`

`R2 R8 F6 E3 B8 A7`

`R2 F2 F6 E6 B4 B8`

`R2 F2 F6 E6 B4 A1`

`R3 R7 F5 E1 E3 A5`

`R3 R7 E1 E3 A5 A3`

`R3 F5 F8 E1 E3 A5`

`R3 F7 E2 E6 B6 A6`

`R3 F8 E1 E3 A5 A3`

`R7 R8 F2 F4 E1 E3`

`R7 R8 F2 E1 E3 B6`

`R7 R8 F3 F5 E1 E3`

`R7 R8 F4 E1 E3 B6`

`R7 R8 F4 E1 E3 A6`

`R7 R8 F4 E1 E3 A3`

`R7 R8 F4 E3 B2 A3`

`R7 F2 F6 E1 E3 A5`

`R8 F1 F3 F6 E3 B8`

`R8 F2 F4 F8 E1 E3`

`R8 F2 F8 E1 E3 B6`

`R8 F3 F5 F8 E1 E3`

`R8 F3 F6 E3 B8 A7`

`R8 F4 F6 E1 E3 B8`

`R8 F4 F8 E1 E3 B6`

`R8 F4 F8 E1 E3 A6`

`R8 F4 F8 E1 E3 A3`

`R8 F4 F8 E3 B2 A3`

`R8 F4 E3 E5 B2 A3`

`F2 F6 F8 E1 E3 A5`

`F7 E2 E6 B1 B6 A6`


**5 pilots 25/15/20**


`R1 R2 R4 R8 F5`

`R1 R2 R8 F5 B3`

`R1 R2 R8 B3 A3`

`R1 R7 R8 F4 B2`

`R1 R7 R8 F4 A7`

`R1 R7 R8 B2 A4`

`R1 R8 F4 F8 B2`

`R1 R8 F4 F8 A7`

`R1 R8 F4 E1 B6`

`R1 R8 F4 E1 A3`

`R1 R8 F8 B2 A4`

`R2 R8 F5 B3 A7`

`R2 R8 F5 B7 A7`

`R2 R8 F5 A7 A2`

`R3 R5 R6 E2 E6`

`R3 R5 F6 E2 E6`

`R3 R5 E2 E6 A2`

`R5 R6 E2 E6 B1`

`R5 F6 E2 E6 B1`

`R5 E2 E6 B1 A2`
