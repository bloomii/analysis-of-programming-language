import sys, string
import numpy as np
from numpy.lib.stride_tricks import sliding_window_view

leet_dict = {
    'A': '@',
    'B': '8',
    'C': '<',
    'D': '?',
    'E': '3',
    'F': '7',
    'G': '9',
    'H': '#',
    'I': '1',
    'J': '}',
    'K': '<',
    'L': '|',
    'M': 'м',
    'N': '^',
    'O': '0',
    'P': '(',
    'Q': '%',
    'R': '®',
    'S': '$',
    'T': '+',
    'U': 'u',
    'V': 'V',
    'W': 'Ш',
    'X': '*',
    'Y': '¥',
    'Z': '5'
}
# Read file to array
# characters = np.array([' ']+list(open("pride-and-prejudice.txt").read())+[' '])
characters = np.array([' ']+list(open(sys.argv[1]).read())+[' '])
# Characters that are not letters become spaces
characters[~np.char.isalpha(characters)] = ' '
# upper
characters = np.char.upper(characters)
characters = np.vectorize(lambda x:leet_dict.get(x,' '))(characters)
# Split the words by finding the indices of spaces
sp = np.where(characters == ' ')
sp2 = np.repeat(sp, 2)
# Get the pairs as a 2D matrix, skip the first and the last
w_ranges = np.reshape(sp2[1:-1], (-1, 2))
# Remove the indexing to the spaces themselves
w_ranges = w_ranges[np.where(w_ranges[:, 1] - w_ranges[:, 0] > 2)]
# Voila! Words are in between spaces, given as pairs of indices
words = list(map(lambda r: characters[r[0]:r[1]], w_ranges))
# Let's recode the characters as strings
swords = np.array(list(map(lambda w: ''.join(w).strip(), words)))
# Get 2-grams (2-grams are all 2 consecutive words in a sequence)
two_grams = sliding_window_view(swords, 2)
# calculate frequency of 2-grams
uniq, counts = np.unique(two_grams, axis=0, return_counts=True)
# sort
wf_sorted = sorted(zip(uniq, counts), key=lambda t: -t[1])
# print
for w, c in wf_sorted[:5]:
    print(f'{w[0]} {w[1]}', '-', c)
