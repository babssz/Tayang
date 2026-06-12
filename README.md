# Tayang
Aplikasi Android untuk menjelajahi, mencari, dan menyimpan film serta serial TV favorit.

## Fitur
- **Autentikasi** — Register & Login dengan penyimpanan lokal
- **Beranda** — Banner trending auto-scroll + Film & Serial populer
- **Jelajah** — Pencarian film dan filter berdasarkan genre
- **Detail Film** — Informasi lengkap + Play Trailer via YouTube
- **Favorit** — Simpan film favorit, bisa diakses offline
- **Profil** — Info akun, statistik, toggle Dark/Light theme
- **Dark & Light Theme** — Bisa diganti sesuai preferensi

## Teknologi
| Komponen | Teknologi |
|---|---|
| Bahasa | Java |
| UI | XML Layout + Material Components |
| Navigasi | Navigation Component |
| Networking | Retrofit2 + OkHttp |
| Load Gambar | Glide |
| Database Lokal | SQLite |
| Preferensi | SharedPreferences |
| API | TMDB (The Movie Database) |

## Arsitektur & Struktur
```
com.example.tayang/
├── activity/
│   ├── SplashActivity      # Halaman awal + cek status login
│   ├── AuthActivity        # Container Login & Register
│   ├── MainActivity        # Container Fragment utama + Bottom Nav
│   └── PlayerActivity      # Putar trailer YouTube
├── fragment/
│   ├── LoginFragment       # Form login
│   ├── RegisterFragment    # Form register
│   ├── HomeFragment        # Beranda + banner + film populer
│   ├── ExploreFragment     # Pencarian + filter genre
│   ├── DetailFragment      # Detail film + favorit
│   ├── FavoriteFragment    # Daftar film tersimpan
│   └── ProfileFragment     # Profil + pengaturan tema
├── adapter/
│   ├── MovieAdapter        # RecyclerView film horizontal
│   ├── MovieGridAdapter    # RecyclerView film grid
│   ├── BannerAdapter       # ViewPager2 banner
│   └── GenreAdapter        # RecyclerView genre chip
├── model/
│   ├── Movie               # Model data film dari API
│   ├── FavoriteMovie       # Model film dari SQLite
│   ├── MovieResponse       # Wrapper response list film
│   ├── Genre               # Model genre
│   ├── GenreResponse       # Wrapper response genre
│   ├── Trailer             # Model trailer YouTube
│   └── VideoResponse       # Wrapper response trailer
├── network/
│   ├── ApiService          # Interface endpoint TMDB API
│   └── ApiClient           # Setup Retrofit instance
└── utils/
    ├── DatabaseHelper      # Setup & operasi SQLite
    ├── Constants           # API key, base URL, dll
    └── TayangApp           # Application class + init tema
```

## Cara Menjalankan
1. Clone repository ini
```bash
git clone https://github.com/username-kamu/Tayang.git
```

2. Buka project di **Android Studio**
3. Daftar API key gratis di [themoviedb.org](https://www.themoviedb.org/settings/api)
4. Buka `Constants.java` dan isi API key:
```java
public static final String API_KEY = "your_api_key_here";
```
5. Run project ke emulator atau device Android (min API 24)

## Spesifikasi Teknis
| Spesifikasi | Detail |
|---|---|
| Minimum SDK | Android 7.0 (API 24) |
| Target SDK | Android 14 (API 34) |
| Bahasa | Java |
| IDE | Android Studio |

## Implementasi Teknis

### Networking
Data film diambil secara real-time dari TMDB API menggunakan **Retrofit2**. Setiap request dijalankan secara asynchronous menggunakan `enqueue()` dengan callback `onResponse` dan `onFailure`. Filter `include_adult=false` diterapkan untuk memastikan konten yang ditampilkan sesuai.

### Local Data
Film favorit disimpan menggunakan **SQLite** melalui `DatabaseHelper`. Data tersimpan lokal sehingga bisa diakses tanpa koneksi internet. Status login dan preferensi tema disimpan menggunakan **SharedPreferences**.

### Navigasi
Aplikasi menggunakan **Navigation Component** dengan dua navigation graph — `nav_auth.xml` untuk alur autentikasi dan `nav_main.xml` untuk alur utama aplikasi.

### Dark/Light Theme
Tema dikelola menggunakan `AppCompatDelegate` dan `DayNight` theme dari Material Components. Preferensi tema disimpan di SharedPreferences dan diterapkan saat app pertama kali dibuka melalui `TayangApp`.
