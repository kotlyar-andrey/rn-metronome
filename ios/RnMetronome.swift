import Foundation
import AVFoundation
import React

@objc(RnMetronome)
class RnMetronome: NSObject {
    var audioEngine: AVAudioEngine!;
    var sampleRate: Float = 44100.0;
    var samplesForOneTik: Int = 22050;
    
    let frequency = 440;
    let amplitude: Float = 0.5;
    
    var counter = 0;
    var bpm = 120;
    
    
    let samplesSounding = 2000;

    var counterTest = 0 // how many times metronome tiks
    var flagTest = 0 // for testing metronome
    
    override init() {
        super.init()
        
        audioEngine = AVAudioEngine();
        
        let mainMixer = audioEngine.mainMixerNode
        let output = audioEngine.outputNode
        let outputFormat = output.inputFormat(forBus: 0)
        sampleRate = Float(outputFormat.sampleRate)
        
        let bps = Float(Float(self.bpm) / 60.0);
        samplesForOneTik = Int(self.sampleRate / bps);
        
        let inputFormat = AVAudioFormat(commonFormat: outputFormat.commonFormat,
                                        sampleRate: outputFormat.sampleRate,
                                        channels: 1,
                                        interleaved: outputFormat.isInterleaved)
        
        audioEngine.attach(sourceNode)

        audioEngine.connect(sourceNode, to: mainMixer, format: inputFormat)
        audioEngine.connect(mainMixer, to: output, format: outputFormat)
        mainMixer.outputVolume = 0.5
    }
    
    private func generateSoundByte(phase: Int) -> Float {
        return sin(3 * Float.pi * Float(phase) / 120) * amplitude;
    }
    
    private func calculationBeats(beatsPerMinute: NSNumber) {
        bpm = Int(beatsPerMinute);
        let bps = Float(Float(self.bpm) / 60.0);
        samplesForOneTik = Int(self.sampleRate / bps);
    }
    
    private lazy var sourceNode = AVAudioSourceNode { _, _, frameCount, audioBufferList -> OSStatus in
        let ablPointer = UnsafeMutableAudioBufferListPointer(audioBufferList)
        
        print(self.bpm, self.samplesForOneTik)
        for frame in 0..<Int(frameCount) {
            self.counter += 1
            var value = self.generateSoundByte(phase: 0);
            if self.counter < self.samplesSounding {
                value = self.generateSoundByte(phase: self.counter)
                if self.flagTest == 0 {
                    self.counterTest += 1;
                    self.flagTest = 1
                }
                
            }
            if self.counter % self.samplesForOneTik == 0 {
                self.counter = 0;
                self.flagTest = 0;
            }
    
            for buffer in ablPointer {
                let buf: UnsafeMutableBufferPointer<Float> = UnsafeMutableBufferPointer(buffer)
                buf[frame] = value
            }
        }
        
        return noErr
    }
    
    @objc
    func play(_ beatsPerMinute: NSNumber) {
        self.calculationBeats(beatsPerMinute: beatsPerMinute);
        do {
            try audioEngine.start()
        } catch {
            print("Could not start engine: \(error.localizedDescription)")
        }
    }
    
    @objc
    public func stop() {
        audioEngine.stop()
    }
}


// @objc(RnMetronome)
// class RnMetronome: NSObject {

//   @objc(multiply:withB:withResolver:withRejecter:)
//   func multiply(a: Float, b: Float, resolve:RCTPromiseResolveBlock,reject:RCTPromiseRejectBlock) -> Void {
//     resolve(a*b)
//   }
// }
