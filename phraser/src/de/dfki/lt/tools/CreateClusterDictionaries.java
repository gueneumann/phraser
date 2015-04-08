package de.dfki.lt.tools;

/**
 * This is the main class for creating dictionaries of phrases which all belong to the same cluster.
 * The cluster files are in /Users/gune00/data/PhrasalClusters
 *
 * Format
The file contains an alphabetical listing of phrases and their cluster memberships. 
Each line is a tab-separated list of the phrase and each cluster it belongs to, 
along with the similarity to each cluster centroid. 
Up to twenty clusters with the highest centroid similarities are included for each phrase.

AutoCAD LT      401     0.260717        736     0.196809        783     0.183296        525     0.177165        808     0.165705        218     0.162994        815     0.141620        97      0.140854        812     0.139386        244     0.127848        111  0.124953 163     0.124443        55      0.123724        324     0.117858        832     0.115209        70      0.113849        197     0.111080        5       0.109667        357     0.107718        833     0.107164

There are 9901505 phrases in total. 
 * 
 * From this, I will create for each cluster a dictionary which contains the phrases and their centroid-similarity.
 * @author gune00
 *
 */
public class CreateClusterDictionaries {

}
