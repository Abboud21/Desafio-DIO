package br.com.dio.repository;

import br.com.dio.expcetion.InvestmentNotFoundException;
import br.com.dio.expcetion.WalletNotFoundException;
import br.com.dio.model.Investment;
import br.com.dio.model.InvestmentWallet;

import java.util.ArrayList;
import java.util.List;

public class investmentRepository {

    private final List<Investment> investments = new ArrayList<>();

    private final List<InvestmentWallet> wallets = new ArrayList<>();

    public Investment findById(final long id){
        return investments.stream().filter(a -> a.id() == id)
                .findFirst()
                .orElseThrow(
                        () -> new InvestmentNotFoundException("O investimento '" + id + " nao foi enconntrado")
                );
    }

    public InvestmentWallet findWalletByAccountPix(final String pix){
        return wallets.stream()
                .filter(w -> w.getAccount().getPix().contains(pix))
                .findFirst()
                .orElseThrow(
                        () -> new WalletNotFoundException("A carteira neo foi encontrada")
                );
    }


    public List<InvestmentWallet> listWallets(){
        return this.wallets;
    }

    public List<Investment> list(){
        return this.investments;
    }

}
