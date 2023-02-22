FROM php:7.4-fpm

# Install Elasticsearch and MongoDB PHP extensions
RUN apt-get update && apt-get install -y \
    libicu-dev \
    libzip-dev \
    zlib1g-dev \
    unzip \
    && pecl install mongodb \
    && docker-php-ext-enable mongodb \
    && docker-php-ext-install intl pdo_mysql zip

ENV COMPOSER_ALLOW_SUPERUSER 1
COPY --from=composer/composer:latest-bin /composer /usr/bin/composer
COPY composer.json /var/www/html    
RUN composer install --no-dev --optimize-autoloader

# Copy project files
COPY . /var/www/html

# Set working directory
WORKDIR /var/www/html

# Expose port 9000 for PHP-FPM
EXPOSE 9000

# Start PHP-FPM service
CMD ["php-fpm"]

