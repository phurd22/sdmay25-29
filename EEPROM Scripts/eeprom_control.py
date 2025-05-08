# Writes EEPROM timing logic signals to .bin file

data = bytearray(128)

for i in range(118):
    data[i] |= 0x80
 
for i in range(100, 128):
    data[i] |= 0x02
    
data[98] |= 0x08
data[99] |= 0x04

data[102] |= 0x10
data[104] |= 0x20

data[106] |= 0x40

for i in range(128):
    middle = data[i] & 0x7C
    flipped = (~middle) & 0x7C
    data[i] = (data[i] & ~0x7C) | flipped
    
data[118] = 0x7F
data[119] = 0x7F
    
with open("eeprom_control.bin", "wb") as f:
    for i in range(128):
        print(f"{i}: {data[i]:02X}\n")
    f.write(data)
    for i in range(63872):
        f.write(bytes([0x00]));