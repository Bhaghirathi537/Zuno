# API and Asset Notes

## Metadata API

PulsePlay uses Apple's public iTunes Search API for metadata discovery.

The implementation reads:

- `trackName`
- `artistName`
- `artworkUrl100`

The API documentation states that the Search API returns JSON and documents these result keys.

Reference:
https://developer.apple.com/library/archive/documentation/AudioVideo/Conceptual/iTuneSearchAPI/

## Audio assets

The three MP3s in `res/raw` were generated specifically for this demo project as simple original synthesized tones.

They are not recordings copied from a commercial song.

For a polished recruitment presentation, you may replace them with music you have permission to use while preserving the same filenames.
