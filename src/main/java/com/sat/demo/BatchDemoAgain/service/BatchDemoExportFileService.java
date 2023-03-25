package com.sat.demo.BatchDemoAgain.service;

import java.io.IOException;
import java.io.Writer;

public interface BatchDemoExportFileService {
    void exportDBtoCSV();
    void exportDBtoTxt();
    void exportDBtoPDF() throws IOException;
}
