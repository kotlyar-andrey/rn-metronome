import React, {useState} from 'react';
import {SafeAreaView, Text, View, Button} from 'react-native';
import RNMetronome from 'rn-metronome';

const App = () => {
  const [bpm, setBpm] = useState(80);

  return (
    <SafeAreaView>
      <View>
        <Text>BPM: {bpm}</Text>
        <Button
          title="Play"
          onPress={() => {
            RNMetronome.play(bpm);
          }}
        />
        <Button
          title="Stop"
          onPress={() => {
            RNMetronome.stop();
          }}
        />
        <Button
          title="+5"
          onPress={() => {
            RNMetronome.stop();
            if (bpm < 1000) {
              setBpm(bpm + 5);
            }
          }}
        />
        <Button
          title="-5"
          onPress={() => {
            RNMetronome.stop();
            if (bpm > 10) {
              setBpm(bpm - 5);
            }
          }}
        />
      </View>
    </SafeAreaView>
  );
};

export default App;
