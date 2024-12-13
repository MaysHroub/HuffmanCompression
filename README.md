# Huffman Compression Tool 🗜️

## Overview
This project implements the Huffman coding algorithm to compress and decompress files of any type. While Huffman coding is efficient for many scenarios, it's important to note that it may occasionally increase the size of certain compressed files due to the nature of encoding.

The tool was developed using `Java` and features a user-friendly graphical interface built with `JavaFX`.

Unlike many other implementations, this project minimizes the use of strings, which are resource-intensive in terms of both memory and processing time, to improve its performance.


## Header of Compressed file
The header contains the following information:
1. Original File Extension.
2. Number of Padding Bits.
3. Header Size (in bits).
4. Huffman Tree Structure.

The Huffman tree is stored using the `Standard Tree Format` (STF) in post-order traversal. Internal nodes are encoded as `0`, while leaf nodes are encoded as `1` followed by the byte value.

### Tree Decoding Algorithm
A `Stack` is used to decode the tree format.
1. If you encounter `1`, read the byte value, create a node, and push it onto the stack.
2. If you encounter `0`, pop two nodes (pop right and pop left), create a parent node, and push it back onto the stack.
3. The stack's final node is the root of the Huffman tree.


## Features
- **Friendly User Interface**: Easy-to-navigate GUI for seamless interaction.
- **Compression Statistics**: Provides insights about compression efficiency.
- **Accurate Decompression**: Ensures files are restored to their original state.
- **Efficient I/O Operations**: Uses buffered reading and writing for performance optimization.


## Screenshots
### Start Screen
![start screen](https://github.com/user-attachments/assets/ee67df91-6e93-48c5-8671-a63468802bff)
### Compressing a file
![compressing](https://github.com/user-attachments/assets/5048b40e-eba2-405b-b43c-fa592fc1fc96)
### Statistics of compression
![stats](https://github.com/user-attachments/assets/c129cc5f-d0e6-49fe-83a0-23b41380b207)
### Huffman Coding Table
![table](https://github.com/user-attachments/assets/e73f9993-d610-439f-9504-b7b9a0d83ce2)
### Header of Compressed file
![header](https://github.com/user-attachments/assets/354bcc30-e535-46de-bf8c-da94004b3961)
### Decompressing a file
![decompressing](https://github.com/user-attachments/assets/2443c1f6-3f6f-4c45-bd84-567c0a857e91)


## Demo Video
[Click me to see the demo (⌐■_■)](https://drive.google.com/file/d/16qEeX8uU1_kAf4DaVjWeMHrGgcEYIxG4/view?usp=sharing)

## Resources
This project was developed with the help of the following resources:

1. [Huffman Howto](https://www.cs.utexas.edu/~scottm/cs314/javacode/A10_Huffman/HuffmanHowTo.htm)
2. [12. 18. Huffman Coding Trees - OpenDSA](https://opendsa-server.cs.vt.edu/ODSA/Books/Everything/html/Huffman.html)
