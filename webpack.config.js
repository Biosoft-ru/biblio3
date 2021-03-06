"use strict";
const webpack = require('webpack');
const path = require('path');
const loaders = require('./webpack.common').loaders;
const externals = require('./webpack.common').externals;
const HtmlWebpackPlugin = require('html-webpack-plugin');
const DashboardPlugin = require('webpack-dashboard/plugin');
const ExtractTextPlugin = require('extract-text-webpack-plugin');

const HOST = process.env.HOST || "127.0.0.1";
const PORT = process.env.PORT || "8888";

loaders.push({
    test: /\.scss$/,
    loaders: ['style-loader', 'css-loader?importLoaders=1', 'sass-loader'],
    exclude: ['node_modules']
});

module.exports = {
    entry: [
        'babel-polyfill',
        'react-hot-loader/patch',
        './src/frontend/scripts/initApp.js'
    ],
    devtool: process.env.WEBPACK_DEVTOOL || 'eval-source-map',
    output: {
        publicPath: '/',
        path: path.join(__dirname, 'public'),
        filename: '[name].js',
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
    devServer: {
        contentBase: "./public",
        // do not print bundle build stats
        noInfo: true,
        // enable HMR
        hot: true,
        // embed the webpack-dev-server runtime into the bundle
        inline: true,
        // serve index.html in place of 404 responses to allow HTML5 history
        historyApiFallback: true,
        port: PORT,
        host: HOST,
        proxy: {
            '/api/*' : {
                target: 'http://localhost:8200/api/',
                secure: false,
                changeOrigin: true,
                pathRewrite: {
                    '^/api': ''
                }
            }
        }
    },
    plugins: [
        new webpack.NoEmitOnErrorsPlugin(),
        new webpack.HotModuleReplacementPlugin(),
        new ExtractTextPlugin({
            filename: 'static/[name]+[hash].css'
        }),
        new DashboardPlugin(),
        new HtmlWebpackPlugin({
            template: './src/frontend/template-dev.html',
            files: {
                css: ['style.css'],
                js: [ "bundle.js"],
            }
        }),
    ],
    externals: externals,
};
