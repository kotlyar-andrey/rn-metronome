import * as React from 'react';

import { StyleSheet, View, Text, Button } from 'react-native';
import { Metronome } from 'rn-metronome';

export default function App() {
  const [bpm, setBpm] = React.useState<number>(200);

  return (
    <View style={styles.container}>
      <Button onPress={() => setBpm(bpm + 1)} title="+" />
      <Text>---------------------</Text>
      <Button onPress={() => setBpm(bpm - 1)} title="-" />
      <Text>---------------------</Text>
      <Text>bpm: {bpm}</Text>
      <Text>---------------------</Text>
      <Button onPress={() => Metronome.play(bpm)} title="Play" />
      <Text>---------------------</Text>
      <Button onPress={() => Metronome.stop()} title="Stop" />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
