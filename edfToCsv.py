import pyedflib
import numpy as np
import pandas as pd
import os

edfDIR = 'dataset/edf'
csvDIR = 'dataset'

for fil in os.listdir(edfDIR):
    if fil.endswith('.edf'):
        filePath = os.path.join(edfDIR, fil)
        f = pyedflib.EdfReader(filePath)
        n = f.signals_in_file
        signal_labels = f.getSignalLabels()
        sigbufs = np.zeros((n, f.getNSamples()[0]))
        for i in np.arange(n):
            sigbufs[i, :] = f.readSignal(i)
        df = pd.DataFrame(sigbufs.T, columns = signal_labels)
        outFile = os.path.join(csvDIR, fil.replace('.edf', '.csv'))
        df.to_csv(outFile, index=None)



