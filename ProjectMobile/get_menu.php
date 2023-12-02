<?php
// Koneksi ke database
$koneksi = new mysqli("localhost", "root", "", "affogatoaffaircafe");

// Mengecek koneksi
if ($koneksi->connect_error) {
    die("Connection failed: " . $koneksi->connect_error);
}

// Membuat array untuk menyimpan hasil
$response = array();
$response["menu"] = array();

// Menjalankan query untuk mengambil data menu
$sql = "SELECT * FROM menu";
$result = $koneksi->query($sql);

if ($result->num_rows > 0) {
    // Mengambil semua data dan menyimpannya dalam array
    while($row = $result->fetch_assoc()) {
        $menu = array();
        $menu["nama"] = $row["nama"];
        $menu["harga"] = $row["harga"];
        $menu["detail"] = $row["detail"];
        $menu["picture"] = $row["picture"]; // Pastikan path gambar dapat diakses oleh aplikasi Android
        array_push($response["menu"], $menu);
    }
    echo json_encode($response);
} else {
    echo "0 results";
}
$koneksi->close();
?>
