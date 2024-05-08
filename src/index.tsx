import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'rn-metronome' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const RnMetronome = NativeModules.RnMetronome
  ? NativeModules.RnMetronome
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export function multiply(a: number, b: number): Promise<number> {
  return RnMetronome.multiply(a, b);
}
