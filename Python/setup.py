import setuptools

with open("README.md", "r") as fh:
    long_description = fh.read()

setuptools.setup(
    name="grabpay-merchant-sdk",
    version="0.0.0",
    author="GrabTaxi Holdings Pte Ltd",
    description="GrabPay Merchant Integration SDK for Python",
    long_description="This SDK is used for supporting merchant integration with GrabPay backend API"
    + long_description,
    long_description_content_type="text/markdown",
    url="https://github.com/grab/grabpay-merchant-sdk",
    packages=setuptools.find_packages(),
    classifiers=[
        "Programming Language :: Python :: 3",
        "Operating System :: OS Independent",
    ],
)
