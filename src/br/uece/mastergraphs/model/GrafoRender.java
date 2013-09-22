package br.uece.mastergraphs.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javafx.scene.image.Image;


public class GrafoRender {
	
	private static final String GRAPHIZ_PATH = "D:\\graphiz\\App";

	public Image render(GrafoEmersonImpl g) throws IOException {
		File dot = gerarArquivoDot(g);
		File img = File.createTempFile("graph", ".png");
		runDot(dot.getAbsolutePath(), "png", img.getAbsolutePath());
		Image i = lerImagem(img);
		return i;
	}
	
	private Image lerImagem(File img) throws FileNotFoundException {
		return new Image("file:" + img.getAbsolutePath(), true);
	}

	public File gerarArquivoDot(GrafoEmersonImpl g) throws IOException {
		File dot = File.createTempFile("graph", ".dot");
		System.out.println("criando dot em " + dot.getAbsolutePath());
		FileOutputStream outDot = new FileOutputStream(dot);
		try {
			outDot.write(g.toString().getBytes());
		} finally {
			outDot.close();
		}
		return dot;
	}
	
	public void runDot(String inputfile, String outputType, String outputFile) throws IOException {
		String cmd = GRAPHIZ_PATH + File.separator + "bin" + File.separator + "dot.exe -T" + outputType + " -o" + outputFile + " " + inputfile;
		System.out.println(cmd);
		try {
			Runtime.getRuntime().exec(cmd).waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
