<?php
// Koneksi ke database
$koneksi = new mysqli("localhost", "root", "", "affogatoaffaircafe");

// Cek koneksi
if ($koneksi->connect_error) {
    die("Koneksi gagal: " . $koneksi->connect_error);
}

// Cek apakah semua data yang diperlukan dikirim melalui POST
if (isset($_POST['phone_number'], $_POST['name'], $_POST['email'], $_POST['gender'], $_POST['birthdate'], $_POST['password'])) {
    $phone_number = $_POST['phone_number'];
    $name = $_POST['name'];
    $email = $_POST['email'];
    $gender = $_POST['gender'];
    $birthdate = $_POST['birthdate'];
    $password = password_hash($_POST['password'], PASSWORD_DEFAULT);

    // Periksa apakah nomor telepon sudah ada di database
    $check = $koneksi->prepare("SELECT phone_number FROM user WHERE phone_number=?");
    $check->bind_param("s", $phone_number);
    $check->execute();
    $result = $check->get_result();

    if ($result->num_rows == 0) {
        echo "Nomor telepon tidak terdaftar.";
        $check->close();
    } else {
        // Nomor telepon sudah ada, lakukan update
        $stmt = $koneksi->prepare("UPDATE user SET name=?, email=?, gender=?, birthdate=?, password=? WHERE phone_number=?");
        $stmt->bind_param("ssssss", $name, $email, $gender, $birthdate, $password, $phone_number);

        // Eksekusi statement
        if ($stmt->execute()) {
            echo "Informasi pengguna berhasil diperbarui";
        } else {
            echo "Terjadi kesalahan saat memperbarui informasi pengguna: " . $stmt->error;
        }
        $stmt->close();
    }
} else {
    echo "Semua data diperlukan.";
}

// Tutup koneksi ke database
$koneksi->close();
?>
