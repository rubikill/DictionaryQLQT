package manager;

import dao.IFinderDAO;
import model.Vocabulary;

public class FinderManagerImpl implements IFinderManager{

	public IFinderDAO finderDAO;
		
	public FinderManagerImpl(IFinderDAO finderDAO) {
		this.finderDAO = finderDAO;
	}

	@Override
	public Vocabulary find(String keyWord) {
		// TODO Auto-generated method stub
		return finderDAO.find(keyWord);
	}
}
