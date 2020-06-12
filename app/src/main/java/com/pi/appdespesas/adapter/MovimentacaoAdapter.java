package com.pi.appdespesas.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pi.appdespesas.R;
import com.pi.appdespesas.model.Movimentacao;

import java.util.List;

public class MovimentacaoAdapter extends RecyclerView.Adapter<MovimentacaoAdapter.MyViewHolder> {
    private List<Movimentacao> movimentacaoList;

    public MovimentacaoAdapter(List<Movimentacao> movimentacaoList) {
        this.movimentacaoList = movimentacaoList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movimentacao_lista, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Movimentacao movimentacao = movimentacaoList.get(position);
        holder.onBind(movimentacao);
        if(movimentacao.getTipo() == "d" || movimentacao.getTipo().equals("d")){
            holder.valor.setTextColor(Color.RED);
        } if(movimentacao.getTipo() == "r" || movimentacao.getTipo().equals("r")){
            holder.valor.setTextColor(Color.GREEN);
        }
    }

    @Override
    public int getItemCount() {
        return movimentacaoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView valor, descricao, titulo, data;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.recyclerTitulo);
            valor = itemView.findViewById(R.id.recyclerValor);
            descricao = itemView.findViewById(R.id.recyclerDescricao);
            data =  itemView.findViewById(R.id.recyclerData);
        }

        private void onBind(Movimentacao movimentacao){
            titulo.setText(movimentacao.getCategoria());
            descricao.setText(movimentacao.getDescricao());
            valor.setText(String.valueOf(movimentacao.getValor()));
            data.setText(movimentacao.getData());

        }
    }
}
