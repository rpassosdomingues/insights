from PIL import Image, ImageDraw, ImageFont
import os
import sys

def resize_image_instagram_profile(image):
    standard_size = (720, 720)
    resized_image = image.resize(standard_size)
    return resized_image

def resize_image_instagram_feed(image, format='square'):
    valid_formats = ['square', 'horizontal', 'vertical']
    if format not in valid_formats:
        raise ValueError("Invalid format. Choose between 'square', 'horizontal', or 'vertical'.")

    if format == 'square':
        size = (1080, 1080)
    elif format == 'horizontal':
        size = (1080, 566)
    else:
        size = (1080, 1350)
    
    resized_image = image.resize(size)
    return resized_image

def resize_image_instagram_stories(image):
    standard_size = (1080, 1920)
    resized_image = image.resize(standard_size)
    return resized_image

def resize_image_facebook_profile(image):
    standard_size = (320, 320)
    resized_image = image.resize(standard_size)
    return resized_image

def resize_image_facebook_feed(image, format='square'):
    valid_formats = ['square', 'horizontal', 'vertical']
    if format not in valid_formats:
        raise ValueError("Invalid format. Choose between 'square', 'horizontal', or 'vertical'.")

    if format == 'square':
        size = (1080, 1080)
    elif format == 'horizontal':
        size = (1200, 630)
    else:
        size = (1080, 1350)
    
    resized_image = image.resize(size)
    return resized_image

def resize_image_facebook_stories(image):
    standard_size = (1080, 1920)
    resized_image = image.resize(standard_size)
    return resized_image

# Function to add margin to the image
def add_margin(image, margin):
    width, height = image.size
    new_width = width + 2 * margin
    new_height = height + 2 * margin
    image_with_margin = Image.new(image.mode, (new_width, new_height), (255, 255, 255))
    image_with_margin.paste(image, (margin, margin))
    return image_with_margin

# Function to add warning text to the image
def add_warning_text(image, text):
    drawing = ImageDraw.Draw(image)
    font = ImageFont.load_default()
    margin = 10
    drawing.text((margin, margin), text, fill=(255, 0, 0), font=font)
    return image

def main():
    input_image_name = input("Enter the input image name (located in input_images directory): ")
    output_directory = input("Enter the output directory for resized images (press Enter for default 'resized_images' directory): ")

    input_image_path = os.path.join("input_images", input_image_name)
    output_directory = output_directory.strip() if output_directory else "resized_images"

    # Load the image
    input_image = Image.open(input_image_path)

    # Resize and process the image for each case
    instagram_profile_image = resize_image_instagram_profile(input_image)
    instagram_feed_image = resize_image_instagram_feed(input_image, format='square')
    instagram_stories_image = resize_image_instagram_stories(input_image)
    
    facebook_profile_image = resize_image_facebook_profile(input_image)
    facebook_feed_image = resize_image_facebook_feed(input_image, format='square')
    facebook_stories_image = resize_image_facebook_stories(input_image)

    # Add margin and warning text to some images
    instagram_stories_image_with_margin = add_margin(instagram_stories_image, 10)
    instagram_stories_image_with_text = add_warning_text(instagram_stories_image_with_margin, "Avoid text in this area")

    # Save the images
    if not os.path.exists(output_directory):
        os.makedirs(output_directory)

    instagram_profile_image.save(os.path.join(output_directory, "instagram_profile_image.jpg"))
    instagram_feed_image.save(os.path.join(output_directory, "instagram_feed_image.jpg"))
    instagram_stories_image_with_text.save(os.path.join(output_directory, "instagram_stories_image.jpg"))

    facebook_profile_image.save(os.path.join(output_directory, "facebook_profile_image.jpg"))
    facebook_feed_image.save(os.path.join(output_directory, "facebook_feed_image.jpg"))
    facebook_stories_image.save(os.path.join(output_directory, "facebook_stories_image.jpg"))

if __name__ == "__main__":
    main()
