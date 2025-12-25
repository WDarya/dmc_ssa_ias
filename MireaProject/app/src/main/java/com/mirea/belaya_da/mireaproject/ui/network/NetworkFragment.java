package com.mirea.belaya_da.mireaproject.ui.network;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.mirea.belaya_da.mireaproject.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class NetworkFragment extends Fragment {
    private TextView tvData;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_network, container, false);
        tvData = view.findViewById(R.id.tvData);
        fetchData();
        return view;
    }

    private void fetchData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.open-meteo.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WeatherService service = retrofit.create(WeatherService.class);
        service.getWeather().enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    tvData.setText("Температура: " + response.body().current_weather.temperature + "°C");
                }
            }
            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                tvData.setText("Ошибка: " + t.getMessage());
            }
        });
    }

    interface WeatherService {
        @GET("v1/forecast?latitude=55.75&longitude=37.62&current_weather=true")
        Call<WeatherResponse> getWeather();
    }

    static class WeatherResponse {
        CurrentWeather current_weather;
    }

    static class CurrentWeather {
        float temperature;
    }
}