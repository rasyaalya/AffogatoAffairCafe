<?php
// Koneksi ke database
$koneksi = new mysqli("localhost", "root", "", "affogatoaffaircafe");

// Cek koneksi
if ($koneksi->connect_error) {
    die("Koneksi gagal: " . $koneksi->connect_error);
}

// Cek apakah nomor telepon dikirim melalui POST
if (isset($_POST['phone_number'])) {
    // Ambil nomor telepon dari POST dan simpan di variabel
    $phone_number = $_POST['phone_number'];

    // Siapkan statement SQL untuk memasukkan nomor telepon
    $stmt = $koneksi->prepare("INSERT INTO user (phone_number) VALUES (?)");
    $stmt->bind_param("s", $phone_number);

    // Eksekusi statement
    if ($stmt->execute()) {
        echo "Nomor telepon berhasil ditambahkan";
    } else {
        echo "Terjadi kesalahan saat menambahkan nomor telepon: " . $stmt->error;
    }

    $stmt->close();
} else {
    echo "Nomor telepon diperlukan.";
}

// Tutup koneksi ke database
$koneksi->close();
?>
