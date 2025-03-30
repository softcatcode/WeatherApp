package com.softcat.weatherapp.jsonMocks

val currentWeatherJson = """
    {
        "location": {
            "name": "Amsterdam",
            "region": "North Holland",
            "country": "Netherlands",
            "lat": 52.374,
            "lon": 4.8897,
            "tz_id": "Europe/Amsterdam",
            "localtime_epoch": 1742922760,
            "localtime": "2025-03-25 18:12"
        },
        "current": {
            "last_updated_epoch": 1742922000,
            "last_updated": "2025-03-25 18:00",
            "temp_c": 9.4,
            "temp_f": 48.9,
            "is_day": 1,
            "condition": {
                "text": "Partly cloudy",
                "icon": "//cdn.weatherapi.com/weather/64x64/day/116.png",
                "code": 1003
            },
            "wind_mph": 7.4,
            "wind_kph": 11.9,
            "wind_degree": 295,
            "wind_dir": "WNW",
            "pressure_mb": 1020.0,
            "pressure_in": 30.12,
            "precip_mm": 0.0,
            "precip_in": 0.0,
            "humidity": 81,
            "cloud": 75,
            "feelslike_c": 7.6,
            "feelslike_f": 45.7,
            "windchill_c": 7.0,
            "windchill_f": 44.7,
            "heatindex_c": 8.9,
            "heatindex_f": 48.1,
            "dewpoint_c": 5.9,
            "dewpoint_f": 42.6,
            "vis_km": 9.0,
            "vis_miles": 5.0,
            "uv": 0.1,
            "gust_mph": 10.2,
            "gust_kph": 16.3
        }
    }
""".trimIndent()