package br.com.suportecpl.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFShape;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import br.com.suportecpl.model.Empresa;
import br.com.suportecpl.model.Estoque;
import br.com.suportecpl.model.Ferramenta;
import br.com.suportecpl.model.Grupo;
import br.com.suportecpl.model.Locacao;
import br.com.suportecpl.repository.GrupoPU;
import br.com.suportecpl.service.EmpresaService;
import br.com.suportecpl.service.FerramentaService;
import br.com.suportecpl.service.GrupoService;

@SessionScoped
@Named
public class Dados implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private FerramentaService ferramentaService;
	@Inject
	private GrupoPU grupoPU;
	@Inject
	private GrupoService grupoService;
	@Inject
	private EmpresaService empresaService;

	public void insereDados() throws NullPointerException, IOException {
		String path = "C:\\Users\\Murilo Medeiros\\Desktop\\Ferramental New Holland 2017 Base (1).xlsx";

		FileInputStream file = new FileInputStream(new File(path));

		XSSFWorkbook workbook = new XSSFWorkbook(file);
		XSSFSheet aplicacao = workbook.getSheetAt(0);
		// <65955 <152

		for (int i = 5; i < 358; i++) {
			Ferramenta ferramenta = new Ferramenta();
			Grupo grupo = new Grupo();
			String aplica = "";
			try {
				DataFormatter formatter = new DataFormatter();
				String codigoNh = formatter.formatCellValue(aplicacao.getRow(i).getCell(2));
				ferramenta.setCodigoNh(codigoNh);
				codigoNh = formatter.formatCellValue(aplicacao.getRow(i).getCell(3));
				ferramenta.setCodigoAntigo(codigoNh);
				ferramenta.setDescricao(aplicacao.getRow(i).getCell(4).toString());
				ferramenta.setPreco(aplicacao.getRow(i).getCell(7).getNumericCellValue());
				//Insere Aplicabilidade
				for(int j = 8; j<23; j++){
					if(!aplicacao.getRow(i).getCell(j).toString().isEmpty()){
						aplica += "," + aplicacao.getRow(3).getCell(j).toString();
					}	
				}
				ferramenta.setAplicabilidade(aplica);
				// insere obrigatoriedade
				if (aplicacao.getRow(i).getCell(5).toString().matches("Matriz")) {
				//	ferramenta.setObrigatoriedade((long) 1);
				}
				if (aplicacao.getRow(i).getCell(5).toString().matches("Matriz e Filial")) {
				//	ferramenta.setObrigatoriedade((long) 2);
				}
				if (aplicacao.getRow(i).getCell(5).toString().matches("Recomendável")) {
				//	ferramenta.setObrigatoriedade((long) 3);
				}
				// Inserir grupo ferramenta
				grupo = grupoPU.grupoPorNome(aplicacao.getRow(i).getCell(6).toString());
				ferramenta.setGrupo(grupo);
				//
				ferramentaService.inserirFerramenta(ferramenta);
				System.out.println("Grupo Inserido");

			} catch (Exception e) {
				// TODO: handle exception
				grupo = new Grupo();
				grupo.setDescricao(aplicacao.getRow(i).getCell(6).toString());
				ferramenta.setGrupo(grupo);
				ferramentaService.inserirFerramenta(ferramenta);
				System.out.println("Grupo Inserido");
				e.printStackTrace();
			}

		}

	}

	public void teste() throws IOException {
		String path = "C:\\Users\\Murilo Medeiros\\Desktop\\Ferramental New Holland 2017 Base (1).xlsx";

		FileInputStream file;
		try {
			file = new FileInputStream(new File(path));
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			List lst = workbook.getAllPictures();
			for (int i = 0; i < lst.size(); i++) {

				PictureData pict = (PictureData) lst.get(i);
				System.out.print(pict.getData());
				String ext = pict.suggestFileExtension();
				byte[] data = pict.getData();
				if (ext.equals("jpeg") || ext.equals("png")) {
					OutputStream out = new FileOutputStream(
							"C:\\Users\\Murilo Medeiros\\testeando\\images\\" + i + "." + ext);
					out.write(data);
					out.close();

				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
