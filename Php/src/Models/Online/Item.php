<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models\Online;

use GrabPay\Merchant\Models\DTO;

class Item extends DTO
{
    /**
     * The category of the item.
     */
    public ?string $category;

    /**
     * The item's image URL on the merchant's website.
     */
    public ?string $imageURL;

    /**
     * Enumerated values. Category list.
     * 1=Men's Wear | 2=Women's Apparel | 3=Men's Bag | 4=Women's Bag | 5=Men's Shoes | 6=Women's Shoes | 7=Mobile & Gadgets | 8=Beauty & Personal Care | 9=Home Appliances | 10=Home & Living
     * 11=Kids Fashion | 12=Toys, Kids & Babies | 13=Video Games | 14=Food & Beverages | 15=Computers & Peripherals | 16=Collectibles, Hobbies & Books | 17=Health & Wellness | 18=Travel & Luggage | 19=Tickets & Vouchers | 20=Watches
     * 21=Jewellery & Accessories | 22=Sports & Outdoors | 23=Automotives | 24=Cameras & Drones | 25=Pet Food & Accessories | 26=Airtime | 27=Credit Bill | 28=Other Bill | 29=Miscellaneous
     */
    public ?int $itemCategory;

    /**
     * The name and description of the item.
     */
    public ?string $itemName;

    /**
     * The price per item. If this value is provided, the calculated value should match the total amount.
     */
    public ?int $price;

    /**
     * The number of items being purchased.
     */
    public ?int $quantity;

    /**
     * The name of the supplier who is supplying the item.
     */
    public ?string $supplierName;

    /**
     * The item's URL on the merchant's website.
     */
    public ?string $url;
}