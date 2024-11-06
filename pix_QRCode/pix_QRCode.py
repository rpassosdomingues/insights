import pypix
import qrcode

def generate_pix_qrcode(merchant_name, pix_key, merchant_city, amount, description):
    payload = f"00020126360014BR.GOV.BCB.PIX0114+55359888455845204000053039865802BR5923Rafael Passos Domingues6009SAO PAULO62140510wuDfvjhMUf63048C32"

    # Create the QR code
    qr = qrcode.QRCode(
        version=1,
        error_correction=qrcode.constants.ERROR_CORRECT_L,
        box_size=10,
        border=4,
    )
    qr.add_data(payload)
    qr.make(fit=True)
    return qr.make_image(fill_color="black", back_color="white")

def save_qr_code_image(img, filename):
    img.save(filename)

def main():
    # Replace the following values with your actual information
    merchant_name = "Rafael Domingues"
    merchant_city = "Alfenas-MG"
    amount = "10.00"
    description = "Quans qui é?"

    # List of PIX keys
    pix_keys = ["35988845584", "another_pix_key", "yet_another_pix_key"]

    for pix_key in pix_keys:
        # Generate the PIX QR Code for each key
        pix_qr_code = generate_pix_qrcode(merchant_name, pix_key, merchant_city, amount, description)

        # Save as an image file with the PIX key in the filename
        save_qr_code_image(pix_qr_code, f"QRCodes/pix_qrcode_{merchant_name}.png")

    print("PIX code generated successfully!")

if __name__ == "__main__":
    main()
