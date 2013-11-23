package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import android.content.Context;
import android.os.Environment;

public class FavoriteHistoryDAO {
	// mac dinh file history.txt la truyen id action 1
	// file favorite.txt idaction: 2
	//lay ten file thao tac thong qua idaction
	public String FileOpen(int idAction){
		String _fileOpen = "";
		
		switch (idAction) {
		case 1:
			_fileOpen = "History.txt";
			break;
		case 2:
			_fileOpen = "Favorite.txt";
			break;
		default:
			break;
		}
		return _fileOpen;
	}
	@SuppressWarnings("deprecation")
	//doc file tu sd cadr( doc file history hoac favorite
	//tham so truyen vao :
	
		//						idaction : file can doc
		//						context 
	public ArrayList<String> ReadFile(int idAction)throws IOException{
		ArrayList<String> _ResultReadFile = new ArrayList<String>();
		String fileOpen =Environment.getExternalStorageDirectory()+"/"+FileOpen(idAction);
		
		try {
			File f = new File(fileOpen);
			FileInputStream fis = new FileInputStream(f);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					fis));

			String str = "";
			StringBuffer buf = new StringBuffer();
			str = reader.readLine();
			while (str != null) {
				_ResultReadFile.add(str);
				buf.append(str + "\n");
				str = reader.readLine();
				
			}
			reader.close();
			fis.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return _ResultReadFile;
	}
	//ghi file tu sd cadr( doc file history hoac favorite
	//tham so truyen vao :
	//						keyword: tu can ghi ra file
	//						idaction : file can luu
	//						context 
	public void WriteFile(String keyWord , int idAction) throws IOException{
		ArrayList<String> _result = ReadFile(idAction);
		String _tempText ="";
		for (String string : _result) {
			_tempText += string + "\n";
		}
		_tempText += keyWord + "\n";
		String fileOpen =Environment.getExternalStorageDirectory()+"/"+ FileOpen(idAction);
		
		try {
			File f = new File(fileOpen);
			FileOutputStream fos = new FileOutputStream(f);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					fos));

			writer.write(_tempText);
			// commit
			writer.flush();

			writer.close();
			fos.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	//xoa toan bo 
	public void DeleteAll(int idAction)
	{
		String fileOpen =Environment.getExternalStorageDirectory()+"/"+ FileOpen(idAction);

		try {
			File f = new File(fileOpen);
			FileOutputStream fos = new FileOutputStream(f);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					fos));

			writer.write("");
			// commit
			writer.flush();

			writer.close();
			fos.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//xoa 1 phan tu
	public void DeleteItem(String word , int idAction) throws IOException{
		ArrayList<String> _result = ReadFile(idAction);
		String _tempText ="";
		for (String string : _result) {
			if(!string.equals(word)){
				_tempText += string + "\n";
			}
		}
		String fileOpen =Environment.getExternalStorageDirectory()+"/"+ FileOpen(idAction);
		
		try {
			File f = new File(fileOpen);
			FileOutputStream fos = new FileOutputStream(f);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					fos));

			writer.write(_tempText);
			// commit
			writer.flush();

			writer.close();
			fos.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//kiem tra da ton tai hay chua
	public Boolean Isexists(String word,int idAction) throws IOException{
		ArrayList<String> _result = ReadFile(idAction);
		String _tempText ="";
		for (String string : _result) {
			if(string.equals(word)){
				return true;
			}
		}
		return false;
	}
	
	public Boolean isFavorite(String word) throws IOException{
		
		return Isexists(word, 2);
	}
}
