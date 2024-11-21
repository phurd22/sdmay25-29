from fpdf import FPDF
#import numpy as np
import random
import sys

numbers = []

if len(sys.argv) == 1:
    lower_bound = -(2**49)
    upper_bound = (2**49) - 1
    numbers = [random.randint(lower_bound, upper_bound) for i in range(30)]
    numbers_string = ",".join(map(str, numbers))
    with open("generated_numbers.txt", "w") as file:
        file.write(numbers_string)
elif len(sys.argv) == 2 and not sys.argv[1].isnumeric:
    filename = sys.argv[1]
    with open(sys.argv[1], "r") as file:
        numbers = file.read().strip().split(",")
        numbers = [int(num) for num in numbers]
else:
    for i in range(1, len(sys.argv)):
        numbers.append(int(sys.argv[i])) 

# Initialize PDF
pdf = FPDF()
pdf.add_page()

# Set dot dimensions and spacing
dot_radius = 2
x_offset = 13
y_offset = 13
x_spacer = (208 - (x_offset * 2)) / 29
y_spacer = (295 - (y_offset * 2)) / 49

# Right side of page x = 208
# Bottom of page y = 295

# Get bit array from numbers
bit_array = []
for number in numbers:
    if number >= 0:
        bit_array.append(format(number, '050b'))
    else:
        bit_array.append(format((1 << 50) + number, '050b'))

# Draw grid based on bit array
for row in range(len(numbers)):
    for col in range(50):
        if bit_array[row][col] == '1':
            x = x_offset + row * x_spacer
            y = y_offset + col * y_spacer
            pdf.set_fill_color(0)
            pdf.ellipse(x, y, dot_radius, dot_radius, "F")
            
# Add labels for least significant bit and first number
label_y = y_offset + 49 * y_spacer
pdf.set_font("Arial", size=8)
pdf.text(x_spacer, label_y + dot_radius, "b0")
pdf.text(x_offset, label_y + dot_radius + y_offset / 2, "n0")

# Save PDF
pdf.output("bit_grid.pdf")