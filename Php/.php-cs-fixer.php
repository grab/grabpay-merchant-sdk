<?php

declare(strict_types=1);
$finder = PhpCsFixer\Finder::create()
    ->ignoreDotFiles(false)
    ->ignoreVCSIgnored(true)
    ->in(__DIR__)
;

$config = new PhpCsFixer\Config();
$config
    ->setRiskyAllowed(true)
    ->setRules([
        '@PHP74Migration'                   => true,
        '@PHP74Migration:risky'             => true,
        '@PHPUnit75Migration:risky'         => true,
        '@PhpCsFixer'                       => true,
        '@PhpCsFixer:risky'                 => true,
        'binary_operator_spaces'            => ['operators' => ['=>' => 'align_single_space_minimal']],
        'array_syntax'                      => ['syntax' => 'short'],
        'concat_space'                      => ['spacing' => 'one'],
        'general_phpdoc_annotation_remove'  => ['annotations' => ['expectedDeprecation']],
        'not_operator_with_successor_space' => true,
        'ordered_class_elements'            => ['sort_algorithm' => 'alpha'],
        'ordered_imports'                   => ['sort_algorithm' => 'alpha'],
        'phpdoc_align'                      => ['align' => 'left'],
        'phpdoc_no_alias_tag'               => false,
        'phpdoc_no_empty_return'            => false,
        'phpdoc_summary'                    => false,
        'single_blank_line_at_eof'          => false,
        'yoda_style'                        => false,
    ])
    ->setFinder($finder)
;

return $config;