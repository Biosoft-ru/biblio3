"use strict";
const webpack = require('webpack');
const path = require('path');
const env  = require('yargs').argv.env; // use --env with webpack 2
const loaders = require('./webpack.common').loaders;
const externals = require('./webpack.common').externals;
const HtmlWebpackPlugin = require('html-webpack-plugin');
const WebpackCleanupPlugin = require('webpack-cleanup-plugin');
const ExtractTextPlugin = require('extract-text-webpack-plugin');
const OptimizeCssAssetsPlugin = require('optimize-css-assets-webpack-plugin');
const BundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin;


loaders.push({
  test: /\.scss$/,
  loader: ExtractTextPlugin.extract({fallback: 'style-loader', use : 'css-loader?sourceMap&localIdentName=[local]___[hash:base64:5]!sass-loader?outputStyle=expanded'}),
  exclude: ['node_modules']
});

const outPath = 'target-frontend';

let fileName = 'static/[name]-[hash].js';
let templateName = 'template-dev.html';

if (env.min) {
  fileName = 'static/[name]-[hash].min.js';
  templateName = 'template.html';
}

let config = {
  entry: {
    app: ['babel-polyfill', './src/frontend/scripts/initApp.js'],
    be5: ['babel-polyfill', './src/frontend/scripts/be5.js']
  },
  output: {
    publicPath: '/',
    path: path.join(__dirname, outPath),
    filename: fileName,
    chunkFilename : 'static/app-[name]-[id].js',
    library:  '[name]'
  },
  resolve: {
    extensions: ['.js', '.jsx'],
    alias: {
      react: path.resolve('node_modules/react'),
    },
  },
  module: {
    loaders
  },
  plugins: [
    //new BundleAnalyzerPlugin(),
    new WebpackCleanupPlugin(),
    new webpack.optimize.OccurrenceOrderPlugin(),
    new ExtractTextPlugin({
      filename: 'static/css/[name]+[hash].css'
    }),
    new OptimizeCssAssetsPlugin(),
    new HtmlWebpackPlugin({
      filename: 'index.html',
      template: './src/frontend/' + templateName,
      chunks: ['commons', 'app'],
      files: {
        css: ['style.css'],
        js: ['bundle.js'],
      }
    }),
    new HtmlWebpackPlugin({
      filename: 'be5/index.html',
      template: './src/frontend/' + templateName,
      chunks: ['commons', 'be5'],
      files: {
        css: ['style.css'],
        js: ['bundle.js'],
      }
    }),
    new webpack.optimize.CommonsChunkPlugin({
      name: "commons",
      // (the commons chunk name)

      filename: "static/commons+[hash].js",
      // (the filename of the commons chunk)

      // minChunks: 3,
      // (Modules must be shared between 3 entries)

      // chunks: ["pageA", "pageB"],
      // (Only use these entries)
    }),
    new webpack.ContextReplacementPlugin(/moment[\/\\]locale$/, /en|ru/),
  ],
  externals: externals,
};

if (env.min) {
  config.plugins.push(new webpack.optimize.UglifyJsPlugin());
  config.plugins.push(new webpack.DefinePlugin({
    'process.env': {
      'NODE_ENV': JSON.stringify('production')
    }
  }));
}

module.exports = config;