# rn-metronome

# Note! For now supported only Android!

## Getting started

`npm install rn-metronome --save`  
or  
`yarn add rn-metronome`

Supported playback from 10 to 1000 beats per minute!

## Usage

```javascript
import RNMetronome from "rn-metronome";

RNMetronome.play(80); //play with 80 bpm

RNMetronome.stop(); // stop playing

RNMetronome.isPlaying((playing) => {
  console.log(playing); // true if metronome is playing
});

RNMetronome.getCurrentBpm((currentBpm) => {
  console.log(currentBpm); // current bpm
});
```

See _example_ folder for more details.
