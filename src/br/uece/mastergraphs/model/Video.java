package br.uece.mastergraphs.model;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;

public class Video {

	private List<Image> quadros = new ArrayList<Image>();
	private int posicao = 0;
	
	public int getPosicao() {
		return posicao;
	}
	
	public void setPosicao(int posicao) {
		if (posicao >= quadros.size()) throw new IllegalArgumentException();
		this.posicao = posicao;
	}

	public void proximoFrame() {
		if (posicao + 1 < quadros.size()) {
			posicao++;	
		}		
	}
	
	public Image getQuadroAtual() {
		return quadros.get(posicao);
	}
	
	public int getDuracao() {
		return quadros.size();
	}
	
	public List<Image> getQuadros() {
		return quadros;
	}
}
